package com.example.gamify_be.Service;

import com.example.gamify_be.Dto.ApiResponse.ApiResponse;
import com.example.gamify_be.Dto.Lesson.LessonRequestDto;
import com.example.gamify_be.Entity.Lesson;
import com.example.gamify_be.Enums.Lesson.LessonStatusEnum;
import com.example.gamify_be.Repository.LessonRepository;
import com.example.gamify_be.Utils.FirebaseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.MultipartProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class LessonService {
    @Autowired
    MultipartProperties multipartProperties;
    @Autowired
    LessonRepository lessonRepository;
    @Autowired
    CheckPointService checkPointService;
    @Autowired
    ChapterService chapterService;
    @Autowired
    UserService userService;
    @Autowired
    FirebaseUtil firebaseUtil;

    //Hàm kiểm tra tiêu đề bài học đã tồn tại chưa
    private boolean isTitleExisted(String title){
        return lessonRepository.findByTitle(title).isPresent();
    }

    //Hàm tìm kiêm bài học theo id
    private Lesson findLessonById(String id){
        Optional<Lesson> lesson = lessonRepository.findById(id);
        return lesson.orElse(null);
    }

    //Hàm tạo bài học mới
    public ResponseEntity<ApiResponse<Lesson>> createLesson(LessonRequestDto req, MultipartFile image) throws IOException {
        //Xác thực thông tin : Tiêu đề
        if (req.getTitle() == null || req.getTitle().isBlank()){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Yêu cầu API thiếu thông tin: Tiêu đề"));
        }

        if (isTitleExisted(req.getTitle())){
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(ApiResponse.error("Tiêu đề bài học đã tồn tại"));
        }

        //Xác thực thông tin : Nội dung bài học
        if (req.getContent() == null || req.getContent().isBlank()){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Yêu cầu API thiếu thông tin: Nội dung bài học"));
        }

        //Xác thực thông tin : Ảnh
        String fileName = "lesson-"+req.getTitle();
        String fileUrl = null;
        if (image != null){
            //Kiểm tra định dạng ảnh
            if (!Objects.equals(image.getContentType(), "image/png") && !Objects.equals(image.getContentType(), "image/jpeg")){
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(ApiResponse.error("Hình ảnh chỉ chấp nhận phần mở rộng là png,jpg,jpeg"));
            }

            //Kiểm tra kích thước ảnh (tối đa 10MB)
            if (image.getSize() > multipartProperties.getMaxFileSize().toBytes()){
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(ApiResponse.error("Hình ảnh chỉ chấp nhận dung lượng tối đa 10MB"));
            }

            try{
                fileUrl = firebaseUtil.uploadImage(image,fileName);
            }catch (Exception e){
                return ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(ApiResponse.error("Lỗi đăng tải ảnh : "+e));
            }
        }



        //Xác thực thông tin : ID chương
        if (!chapterService.isExistByID(req.getChapterId())){
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("ID chương không tồn tại"));
        }

        //Xác thực thông tin người thao tác
        if (req.getOperator() == null){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Yêu cầu API thiếu thông tin : ID người thao tác"));
        }

        String operatorName = userService.getUsernameById(req.getOperator());
        if (operatorName == null){
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("ID người thao tác không tồn tại"));
        }

        //Tạo đối tượng bài học mới
        assert image != null;
        Lesson nLesson = new Lesson(req.getChapterId(), req.getTitle(), req.getContent(), fileUrl, operatorName);

        //Lưu bài học xuống CSDL
        lessonRepository.save(nLesson);
        return ResponseEntity.ok(ApiResponse.success("Tạo bài học thành công",nLesson));
    }

    //Lấy tất cả dữ liệu bài học
    public ResponseEntity<ApiResponse<List<Lesson>>> getAllLesson() {
        return ResponseEntity.ok(ApiResponse.success("Lấy tất cả dữ liệu bài học thành công", lessonRepository.findAll()));
    }

    //Lấy dữ liệu bài học bằng id
    public ResponseEntity<ApiResponse<Lesson>> getLessonById(String id) {
        Optional<Lesson> lesson = lessonRepository.findById(id);
        return lesson.map(value -> ResponseEntity.ok(ApiResponse.success("Lấy dữ liệu bài học theo id thành công", value)))
                .orElseGet(() -> ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Không tìm thấy dữ liệu bài học theo id : " + id)));
    }

    //Lấy dữ liệu bài học theo id ải
    public ResponseEntity<ApiResponse<List<Lesson>>> getLessonByCPId(String checkPointId) {
        return ResponseEntity.ok(ApiResponse.success("Lấy tất cả dữ liệu bài học theo ải thành công", lessonRepository.findAllByCheckPointId(checkPointId)));
    }

    //Chỉnh sửa nội dung bài học
    public ResponseEntity<ApiResponse<Lesson>> updateLesson(String lessonId, LessonRequestDto req, MultipartFile image){
        //Tìm kiếm đối tượng bài học
        Lesson lesson = findLessonById(lessonId);
        if(lesson == null){
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("ID bài học không tồn tại"));
        }

        //Xác thực tiêu đề bài học
        if (req.getTitle() != null){
            if (isTitleExisted(req.getTitle())){
                return ResponseEntity
                        .status(HttpStatus.CONFLICT)
                        .body(ApiResponse.error("Tiêu đề bài học đã tồn tại"));
            }
            lesson.setTitle(req.getTitle());
        }

        //Xác thực nội dung bài học
        if (req.getContent() != null){
            lesson.setContent(req.getContent());
        }

        //Xác thực chương
        if (req.getChapterId() != null){
            if (!chapterService.isExistByID(req.getChapterId())){
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(ApiResponse.error("ID chương không tồn tại"));
            }
            lesson.setChapterId(req.getChapterId());
        }

        //Xác thực ảnh
        String fileName = "lesson-"+req.getTitle();
        if (image != null){
            //Kiểm tra định dạng ảnh
            if (!Objects.equals(image.getContentType(), "image/png") && !Objects.equals(image.getContentType(), "image/jpeg")){
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(ApiResponse.error("Hình ảnh chỉ chấp nhận phần mở rộng là png,jpg,jpeg"));
            }

            //Kiểm tra kích thước ảnh (tối đa 10MB)
            if (image.getSize() > multipartProperties.getMaxFileSize().toBytes()){
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(ApiResponse.error("Hình ảnh chỉ chấp nhận dung lượng tối đa 10MB"));
            }
            lesson.setImg(fileName);
        }


        //Xác thực người cập nhật
        if (req.getOperator() == null){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Yêu cầu API thiếu thông tin : ID người thao tác"));
        }

        String operatorName = userService.getUsernameById(req.getOperator());
        if (operatorName == null){
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("ID người thao tác không tồn tại"));
        }
        lesson.setUpdatedBy(operatorName);

        //Lưu thông tin cập nhật xuống CSDL
        lessonRepository.save(lesson);
        return ResponseEntity.ok(ApiResponse.success("Cập nhật thông tin bài học thành công",lesson));
    }

    //Cập nhật bài học vào ải
    public ResponseEntity<ApiResponse<Lesson>> setLessonToCP(String lessonId, String CPId){
        Lesson lesson = findLessonById(lessonId);
        if (lesson == null){
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("ID bài học không tồn tại"));
        }

        if (!checkPointService.isExistedById(CPId)){
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("ID ải không tồn tại"));
        }

        lesson.setCheckPointId(CPId);
        return ResponseEntity.ok(ApiResponse.success("Cập nhật bài học vào ải thành công", lesson));
    }

    //Dời bài học ra khỏi ải
    public ResponseEntity<ApiResponse<Lesson>> removeLessonFromCP(String lessonId){
        Lesson lesson = findLessonById(lessonId);
        if (lesson == null){
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("ID bài học không tồn tại"));
        }
        lesson.setCheckPointId(null);
        lessonRepository.save(lesson);
        return ResponseEntity.ok(ApiResponse.success("Cập nhật dời bài học khỏi ải thành công",lesson));
    }

    //Xoá bài học
    public ResponseEntity<ApiResponse<?>> deleteLesson(String lessonId){
        Lesson lesson = findLessonById(lessonId);
        if (lesson == null){
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("ID bài học không tồn tại"));
        }
        lessonRepository.deleteById(lessonId);
        return ResponseEntity.ok(ApiResponse.success("Xoá bài học thành công", null));
    }

    //Cập nhật trạng thái
    public ResponseEntity<ApiResponse<Lesson>> updateStatus(String lessonId){
        Optional<Lesson> optLesson = lessonRepository.findById(lessonId);
        if (optLesson.isEmpty()){
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Không tìm thấy bài học tương ứng"));
        }
        Lesson lesson = optLesson.get();
        if (lesson.getStatus() == LessonStatusEnum.active){
            lesson.setStatus(LessonStatusEnum.inactive);
        }else{
            lesson.setStatus(LessonStatusEnum.active);
        }
        lessonRepository.save(lesson);
        return ResponseEntity.ok(ApiResponse.success("Cập nhật trạng thái thành công",lesson));
    }
}
