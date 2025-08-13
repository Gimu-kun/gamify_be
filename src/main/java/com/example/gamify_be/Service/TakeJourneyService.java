package com.example.gamify_be.Service;

import com.example.gamify_be.Dto.ApiResponse.ApiResponse;
import com.example.gamify_be.Entity.Journey;
import com.example.gamify_be.Entity.TakeJourney;
import com.example.gamify_be.Entity.User;
import com.example.gamify_be.Entity.id.TakeJourneyId;
import com.example.gamify_be.Repository.TakeJourneyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TakeJourneyService {
    @Autowired
    TakeJourneyRepository takeJourneyRepository;
    @Autowired
    UserService userService;
    @Autowired
    JourneyService journeyService;

    //Hàm tìm kiếm quan hệ theo ID
    private boolean isTakeJourneyExisted(String userId,String journeyId){
        TakeJourneyId id = new TakeJourneyId(userId,journeyId);
        return takeJourneyRepository.findById(id).isPresent();
    }

    //Hàm tạo quan hệ tham gia lộ trình
    public ResponseEntity<ApiResponse<?>> createTakeJourney(String userId, String journeyId){
        //Kiểm tra quan hệ đã tồn tại chưa
        if (isTakeJourneyExisted(userId,journeyId)){
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .body(ApiResponse.error("Quan hệ đã tồn tại"));
        }

        //Kiểm tra người dùng có tồn tại không
        User user = userService.getUserById(userId);
        if (user == null){
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Không tìm thấy người dùng tương ứng"));
        }

        //Kiểm tra lộ trình có tồn tại không
        Journey journey = journeyService.getJourneyById(journeyId);
        if (journey == null){
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Không tìm thấy lộ trình tương ứng"));
        }

        //Tạo quan hệ
        TakeJourney nTakeJourney = new TakeJourney(user,journey);
        //Lưu xuống CSDL
        try{
            takeJourneyRepository.save(nTakeJourney);
            return ResponseEntity.ok(ApiResponse.success("Tham gia lộ trình thành công", null));
        }catch(Exception e){
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Lỗi trong quá trình khởi tạo quan hệ tk_jn"));
        }
    }

    //Hàm tìm kiếm danh sách người dùng đã tham gia lộ trình
    public ResponseEntity<ApiResponse<List<User>>> getAllTakenUserByJourney(String journeyId) {
        //Lấy ra các mối quan hệ có id lộ trình theo yêu cầu
        List<TakeJourney> takeJourneys = takeJourneyRepository.findByJourneyId(journeyId);
        //Ánh xạ lấy ra thông tin người dùng từ danh sách lộ trình đã lấy ra
        List<User> users = takeJourneys.stream()
                .map(TakeJourney::getUser)
                .toList();
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách người dùng tham gia thành công", users));
    }

    //Hàm tìm kiếm tất cả lộ trình người dùng chỉ định đã tham gia
    public ResponseEntity<ApiResponse<List<Journey>>> getAllTakenJourneyByUser(String userId){
        //Lấy ra danh sách quan hệ theo id người dùng
        List<TakeJourney> takeJourneys = takeJourneyRepository.findByUserId(userId);
        //Ánh xạ tìm ra danh sách lộ trình trong danh sách quan hệ đã lấy ra
        List<Journey> journeys = takeJourneys.stream()
                .map(TakeJourney::getJourney)
                .toList();
        return ResponseEntity.ok(ApiResponse.success("Lấy thông tin danh sách lộ trình thành công",journeys));
    }
}
