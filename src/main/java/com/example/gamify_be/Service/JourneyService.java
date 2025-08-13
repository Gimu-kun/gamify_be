package com.example.gamify_be.Service;

import com.example.gamify_be.Dto.ApiResponse.ApiResponse;
import com.example.gamify_be.Dto.Journey.JourneyRequestDto;
import com.example.gamify_be.Entity.Journey;
import com.example.gamify_be.Entity.User;
import com.example.gamify_be.Repository.JourneyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

@Service
public class JourneyService {
    @Autowired
    JourneyRepository journeyRepository;

    @Autowired
    UserService userService;

    //Kiểm tra tiêu đề đã tồn tại hay chưa
    private boolean isTitleExisted (String title){
        Optional<Journey> journey = journeyRepository.findByTitle(title);
        return journey.isPresent();
    }

    //Hàm tìm kiếm đối tượng lộ trình bằng số thứ tự
    private Journey getJourneyByOrd(Integer ord){
        Optional<Journey> journey = journeyRepository.findByOrd(ord);
        return journey.orElse(null);
    }

    //Hàm tìm kiếm đối tượng lộ trình bằng id
    public Journey getJourneyById(String id){
        Optional<Journey> journey = journeyRepository.findById(id);
        return journey.orElse(null);
    }


    //Tạo mới lộ trình
    public ResponseEntity<ApiResponse<Journey>> createJourney(JourneyRequestDto req){
        //Tên lộ trình không được trùng
        if (isTitleExisted(req.getTitle())){
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(ApiResponse.error("Tiêu đề lộ trình đã được sử dụng!"));
        }

        //Truy xuất tên tài khoản người thao tác
        User user = userService.getUserById(req.getOperator());
        if (user == null){
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("ID người thao tác không đúng!"));
        }

        //Tạo mới lộ trình và lưu vào CSDL
        try{
            Journey nJourney = new Journey(req.getTitle(),req.getDescription(),user.getUsername());
            journeyRepository.save(nJourney);
            return ResponseEntity.ok(ApiResponse.success("Tạo lộ trình thành công",nJourney));
        }catch(Exception e){
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Lỗi trong quá trình tạo lộ trình :" + e));
        }
    }

    //Cập nhật thứ tự lộ trình
    public ResponseEntity<ApiResponse<Journey>> setOrder(String journeyId, Integer ord){
        Journey fromJourney = getJourneyById(journeyId);
        Journey toJourney = getJourneyByOrd(ord);

        //Không tìm thấy lộ trình đối tượng => id lộ trình sai
        if (fromJourney == null){
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Không tìm thấy lộ trình đối tượng"));
        }

        //Không tìm thấy lộ trình đích => đổi thứ tự trực tiếp
        if (toJourney == null){
            try{
                fromJourney.setOrd(ord);
                journeyRepository.save(fromJourney);
                return ResponseEntity.ok(ApiResponse.success("Cập nhật thứ tự thành công",fromJourney));
            }catch(Exception e){
                return ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(ApiResponse.error("Lỗi trong quá trình cập nhật : " + e));
            }
        }

        //ID lộ trình đối tượng và lộ trình đích giống nhau => thứ tự không thay đổi
        if (Objects.equals(fromJourney.getId(), toJourney.getId())){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Thứ tự lộ trình không thay đổi"));
        }

        //Thứ tự lộ trình đối tượng thay đổi , thứ tự lộ trình đích về null
        try{
            fromJourney.setOrd(toJourney.getOrd());
            toJourney.setOrd(null);
            journeyRepository.saveAll(Arrays.asList(fromJourney,toJourney));
            return ResponseEntity.ok(ApiResponse.success("Cập nhật thứ tự thành công",fromJourney));
        }catch(Exception e){
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Lỗi trong quá trình cập nhật : " + e));
        }
    }

    //Cập nhật nội dung lộ trình
    public ResponseEntity<ApiResponse<Journey>> updateJourney(String journeyId,JourneyRequestDto req){
        //Truy xuất đối tượng lộ trình
        Journey journey = getJourneyById(journeyId);
        if (journey == null){
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Không tìm thấy lộ trình tương ứng"));
        }
        //Kiểm tra các trường dữ liệu đầu vào
        //Tên lộ trình không được trùng
        if (req.getTitle() != null){
            if (isTitleExisted(req.getTitle())){
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(ApiResponse.error("Tiêu đề lộ trình đã được sử dụng!"));
            }
            journey.setTitle(req.getTitle());
        }

        //Truy xuất tên tài khoản người thao tác
        if (req.getOperator() == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Cập nhật lộ trình cần ID người thao tác"));
        }

        User user = userService.getUserById(req.getOperator());
        //Không tìm thấy thông tin người thao tác => id không đúng
        if (user == null){
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("ID người thao tác không đúng!"));
        }
        journey.setUpdated_by(user.getUsername());

        //Cập nhật mô tả nếu có
        if (req.getDescription() != null){
            journey.setDescription(req.getDescription());
        }

        //Cập nhật thông tin lộ trình xuống CSDL
        try{
            journeyRepository.save(journey);
            return ResponseEntity.ok(ApiResponse.success("Cập nhật lộ trình thành công",journey));
        }catch (Exception e){
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Lỗi trong quá trình cập nhật lộ trình : " + e));
        }
    }
}
