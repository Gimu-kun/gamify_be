package com.example.gamify_be.Service;

import com.example.gamify_be.Dto.ApiResponse.ApiResponse;
import com.example.gamify_be.Entity.Roadmap;
import com.example.gamify_be.Entity.TakeRoadmap;
import com.example.gamify_be.Entity.User;
import com.example.gamify_be.Entity.id.TakeRoadmapId;
import com.example.gamify_be.Repository.TakeRoadmapRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TakeRoadmapService {
    @Autowired
    TakeRoadmapRepository takeRoadmapRepository;
    @Autowired
    UserService userService;
    @Autowired
    RoadmapService roadmapService;

    //Hàm tìm kiếm quan hệ theo ID
    private boolean isTakeRoadmapExisted(String userId,String roadmapId){
        TakeRoadmapId id = new TakeRoadmapId(userId,roadmapId);
        return takeRoadmapRepository.findById(id).isPresent();
    }

    //Hàm tạo quan hệ tham gia lộ trình
    public ResponseEntity<ApiResponse<?>> createTakeRoadmap(String userId, String roadmapId){
        //Kiểm tra quan hệ đã tồn tại chưa
        if (isTakeRoadmapExisted(userId,roadmapId)){
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
        Roadmap roadmap = roadmapService.getRoadmapById(roadmapId);
        if (roadmap == null){
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Không tìm thấy lộ trình tương ứng"));
        }

        //Tạo quan hệ
        TakeRoadmap nTakeRoadmap = new TakeRoadmap(user, roadmap);
        //Lưu xuống CSDL
        try{
            takeRoadmapRepository.save(nTakeRoadmap);
            return ResponseEntity.ok(ApiResponse.success("Tham gia lộ trình thành công", null));
        }catch(Exception e){
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Lỗi trong quá trình khởi tạo quan hệ tk_jn"));
        }
    }

    //Hàm tìm kiếm danh sách người dùng đã tham gia lộ trình
    public ResponseEntity<ApiResponse<List<User>>> getAllTakenUserByRoadmap(String roadmapId) {
        //Lấy ra các mối quan hệ có id lộ trình theo yêu cầu
        List<TakeRoadmap> takeRoadmaps = takeRoadmapRepository.findByRoadmapId(roadmapId);
        //Ánh xạ lấy ra thông tin người dùng từ danh sách lộ trình đã lấy ra
        List<User> users = takeRoadmaps.stream()
                .map(TakeRoadmap::getUser)
                .toList();
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách người dùng tham gia thành công", users));
    }

    //Hàm tìm kiếm tất cả lộ trình người dùng chỉ định đã tham gia
    public ResponseEntity<ApiResponse<List<Roadmap>>> getAllTakenRoadmapByUser(String userId){
        //Lấy ra danh sách quan hệ theo id người dùng
        List<TakeRoadmap> takeRoadmaps = takeRoadmapRepository.findByUserId(userId);
        //Ánh xạ tìm ra danh sách lộ trình trong danh sách quan hệ đã lấy ra
        List<Roadmap> roadmaps = takeRoadmaps.stream()
                .map(TakeRoadmap::getroadmap)
                .toList();
        return ResponseEntity.ok(ApiResponse.success("Lấy thông tin danh sách lộ trình thành công", roadmaps));
    }
}
