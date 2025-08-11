package com.example.gamify_be.Service;

import com.example.gamify_be.Dto.ApiResponse.ApiResponse;
import com.example.gamify_be.Dto.Lesson.LessonRequestDto;
import com.example.gamify_be.Entity.Lesson;
import com.example.gamify_be.Repository.LessonRepository;
import com.example.gamify_be.Utils.FirebaseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.MultipartProperties;
import org.springframework.http.HttpStatus;
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
    public ApiResponse<Lesson> createLesson(LessonRequestDto req, MultipartFile image) throws IOException {
        //Xác thực thông tin : Tiêu đề
        if (req.getTitle() == null || req.getTitle().isBlank()){
            return ApiResponse.error("Yêu cầu API thiếu thông tin: Tiêu đề",HttpStatus.BAD_REQUEST);
        }

        if (isTitleExisted(req.getTitle())){
            return ApiResponse.error("Tiêu đề bài học đã tồn tại",HttpStatus.CONFLICT);
        }

        //Xác thực thông tin : Nội dung bài học
        if (req.getContent() == null || req.getContent().isBlank()){
            return ApiResponse.error("Yêu cầu API thiếu thông tin: Nội dung bài học",HttpStatus.BAD_REQUEST);
        }

        //Xác thực thông tin : Ảnh
        String fileName = "lesson-"+req.getTitle();
        String fileUrl = null;
        if (image != null){
            //Kiểm tra định dạng ảnh
            if (!Objects.equals(image.getContentType(), "image/png") && !Objects.equals(image.getContentType(), "image/jpeg")){
                return ApiResponse.error("Hình ảnh chỉ chấp nhận phần mở rộng là png,jpg,jpeg",HttpStatus.BAD_REQUEST);
            }

            //Kiểm tra kích thước ảnh (tối đa 10MB)
            if (image.getSize() > multipartProperties.getMaxFileSize().toBytes()){
                return ApiResponse.error("Hình ảnh chỉ chấp nhận dung lượng tối đa 10MB",HttpStatus.BAD_REQUEST);
            }

            try{
                fileUrl = firebaseUtil.uploadImage(image,fileName);
            }catch (Exception e){
                return ApiResponse.error("Lỗi đăng tải ảnh : "+e,HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }



        //Xác thực thông tin : ID chương
        if (!chapterService.isExistByID(req.getChapterId())){
            return ApiResponse.error("ID chương không tồn tại", HttpStatus.NOT_FOUND);
        }

        //Xác thực thông tin người thao tác
        if (req.getOperator() == null){
            return ApiResponse.error("Yêu cầu API thiếu thông tin : ID người thao tác", HttpStatus.BAD_REQUEST);
        }

        String operatorName = userService.getUsernameById(req.getOperator());
        if (operatorName == null){
            return ApiResponse.error("ID người thao tác không tồn tại", HttpStatus.NOT_FOUND);
        }

        //Tạo đối tượng bài học mới
        assert image != null;
        Lesson nLesson = new Lesson(req.getChapterId(), req.getTitle(), req.getContent(), fileUrl, operatorName);

        //Lưu bài học xuống CSDL
        lessonRepository.save(nLesson);
        return ApiResponse.success("Tạo bài học thành công",nLesson);
    }

    //Lấy tất cả dữ liệu bài học
    public ApiResponse<List<Lesson>> getAllLesson() {
        return ApiResponse.success("Lấy tất cả dữ liệu bài học thành công", lessonRepository.findAll());
    }

    //Lấy dữ liệu bài học bằng id
    public ApiResponse<Lesson> getLessonById(String id) {
        Optional<Lesson> lesson = lessonRepository.findById(id);
        return lesson.map(value -> ApiResponse.success("Lấy dữ liệu bài học theo id thành công", value))
                .orElseGet(() -> ApiResponse.error("Không tìm thấy dữ liệu bài học theo id : " + id, HttpStatus.NOT_FOUND));
    }

    //Lấy dữ liệu bài học theo id ải
    public ApiResponse<List<Lesson>> getLessonByCPId(String checkPointId) {
        return ApiResponse.success("Lấy tất cả dữ liệu bài học theo ải thành công", lessonRepository.findAllByCheckPointId(checkPointId));
    }

    //Chỉnh sửa nội dung bài học
    public ApiResponse<Lesson> updateLesson(String lessonId, LessonRequestDto req, MultipartFile image){
        //Tìm kiếm đối tượng bài học
        Lesson lesson = findLessonById(lessonId);
        if(lesson == null){
            return ApiResponse.error("ID bài học không tồn tại",HttpStatus.NOT_FOUND);
        }

        //Xác thực tiêu đề bài học
        if (req.getTitle() != null){
            if (isTitleExisted(req.getTitle())){
                return ApiResponse.error("Tiêu đề bài học đã tồn tại",HttpStatus.CONFLICT);
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
                return ApiResponse.error("ID chương không tồn tại", HttpStatus.NOT_FOUND);
            }
            lesson.setChapterId(req.getChapterId());
        }

        //Xác thực ảnh
        String fileName = "lesson-"+req.getTitle();
        if (image != null){
            //Kiểm tra định dạng ảnh
            if (!Objects.equals(image.getContentType(), "image/png") && !Objects.equals(image.getContentType(), "image/jpeg")){
                return ApiResponse.error("Hình ảnh chỉ chấp nhận phần mở rộng là png,jpg,jpeg",HttpStatus.BAD_REQUEST);
            }

            //Kiểm tra kích thước ảnh (tối đa 10MB)
            if (image.getSize() > multipartProperties.getMaxFileSize().toBytes()){
                return ApiResponse.error("Hình ảnh chỉ chấp nhận dung lượng tối đa 10MB",HttpStatus.BAD_REQUEST);
            }
            lesson.setImg(fileName);
        }


        //Xác thực người cập nhật
        if (req.getOperator() == null){
            return ApiResponse.error("Yêu cầu API thiếu thông tin : ID người thao tác", HttpStatus.BAD_REQUEST);
        }

        String operatorName = userService.getUsernameById(req.getOperator());
        if (operatorName == null){
            return ApiResponse.error("ID người thao tác không tồn tại", HttpStatus.NOT_FOUND);
        }
        lesson.setUpdatedBy(operatorName);

        //Lưu thông tin cập nhật xuống CSDL
        lessonRepository.save(lesson);
        return ApiResponse.success("Cập nhật thông tin bài học thành công",lesson);
    }

    //Cập nhật bài học vào ải
    public ApiResponse<Lesson> setLessonToCP(String lessonId, String CPId){
        Lesson lesson = findLessonById(lessonId);
        if (lesson == null){
            return ApiResponse.error("ID bài học không tồn tại",HttpStatus.NOT_FOUND);
        }

        if (!checkPointService.isExistedById(CPId)){
            return ApiResponse.error("ID ải không tồn tại",HttpStatus.NOT_FOUND);
        }

        lesson.setCheckPointId(CPId);
        return ApiResponse.success("Cập nhật bài học vào ải thành công", lesson);
    }

    //Dời bài học ra khỏi ải
    public ApiResponse<Lesson> removeLessonFromCP(String lessonId){
        Lesson lesson = findLessonById(lessonId);
        if (lesson == null){
            return ApiResponse.error("ID bài học không tồn tại",HttpStatus.NOT_FOUND);
        }
        lesson.setCheckPointId(null);
        lessonRepository.save(lesson);
        return ApiResponse.success("Cập nhật dời bài học khỏi ải thành công",lesson);
    }

    //Xoá bài học
    public ApiResponse<?> deleteLesson(String lessonId){
        Lesson lesson = findLessonById(lessonId);
        if (lesson == null){
            return ApiResponse.error("ID bài học không tồn tại",HttpStatus.NOT_FOUND);
        }
        lessonRepository.deleteById(lessonId);
        return ApiResponse.success("Xoá bài học thành công", null);
    }
}
