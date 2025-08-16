package com.example.gamify_be.Service;

import com.example.gamify_be.Dto.ApiResponse.ApiResponse;
import com.example.gamify_be.Dto.Roadmap.RoadmapRequestDto;
import com.example.gamify_be.Entity.Roadmap;
import com.example.gamify_be.Entity.User;
import com.example.gamify_be.Enums.Roadmap.RoadmapStatus;
import com.example.gamify_be.Repository.RoadmapRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class RoadmapService {
    @Autowired
    RoadmapRepository roadmapRepository;

    @Autowired
    UserService userService;

    //Kiểm tra tiêu đề đã tồn tại hay chưa
    private boolean isTitleExisted (String title){
        Optional<Roadmap> Roadmap = roadmapRepository.findByTitle(title);
        return Roadmap.isPresent();
    }

    //Hàm tìm kiếm đối tượng lộ trình bằng số thứ tự
    private Roadmap getRoadmapByOrd(Integer ord){
        Optional<Roadmap> Roadmap = roadmapRepository.findByOrd(ord);
        return Roadmap.orElse(null);
    }

    //Hàm tìm kiếm đối tượng lộ trình bằng id
    public Roadmap getRoadmapById(String id){
        Optional<Roadmap> Roadmap = roadmapRepository.findById(id);
        return Roadmap.orElse(null);
    }

    //Tạo mới lộ trình
    public ResponseEntity<ApiResponse<Roadmap>> createRoadmap(RoadmapRequestDto req){
        System.out.println(req.toString());
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
            Roadmap nRoadmap = new Roadmap(req.getTitle(),req.getDescription(),user.getUsername());

            roadmapRepository.save(nRoadmap);
            return ResponseEntity.ok(ApiResponse.success("Tạo lộ trình thành công", nRoadmap));
        }catch(Exception e){
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Lỗi trong quá trình tạo lộ trình :" + e));
        }
    }

    //Cập nhật thứ tự lộ trình
    public ResponseEntity<ApiResponse<Roadmap>> setOrder(String RoadmapId, Integer ord){
        Roadmap fromRoadmap = getRoadmapById(RoadmapId);
        Roadmap toRoadmap = getRoadmapByOrd(ord);

        //Không tìm thấy lộ trình đối tượng => id lộ trình sai
        if (fromRoadmap == null){
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Không tìm thấy lộ trình đối tượng"));
        }

        //Không tìm thấy lộ trình đích => đổi thứ tự trực tiếp
        if (toRoadmap == null){
            try{
                fromRoadmap.setOrd(ord);
                roadmapRepository.save(fromRoadmap);
                return ResponseEntity.ok(ApiResponse.success("Cập nhật thứ tự thành công", fromRoadmap));
            }catch(Exception e){
                return ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(ApiResponse.error("Lỗi trong quá trình cập nhật : " + e));
            }
        }

        //ID lộ trình đối tượng và lộ trình đích giống nhau => thứ tự không thay đổi
        if (Objects.equals(fromRoadmap.getId(), toRoadmap.getId())){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Thứ tự lộ trình không thay đổi"));
        }

        //Thứ tự lộ trình đối tượng thay đổi , thứ tự lộ trình đích về null
        try{
            fromRoadmap.setOrd(toRoadmap.getOrd());
            toRoadmap.setOrd(null);
            roadmapRepository.saveAll(Arrays.asList(fromRoadmap, toRoadmap));
            return ResponseEntity.ok(ApiResponse.success("Cập nhật thứ tự thành công", fromRoadmap));
        }catch(Exception e){
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Lỗi trong quá trình cập nhật : " + e));
        }
    }

    //Cập nhật nội dung lộ trình
    public ResponseEntity<ApiResponse<Roadmap>> updateRoadmap(String RoadmapId, RoadmapRequestDto req){
        //Truy xuất đối tượng lộ trình
        Roadmap roadmap = getRoadmapById(RoadmapId);
        if (roadmap == null){
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Không tìm thấy lộ trình tương ứng"));
        }
        //Kiểm tra các trường dữ liệu đầu vào
        //Tên lộ trình không được trùng
        if (req.getTitle() != null && !req.getTitle().equals(roadmap.getTitle())){
            if (isTitleExisted(req.getTitle())){
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(ApiResponse.error("Tiêu đề lộ trình đã được sử dụng!"));
            }
            roadmap.setTitle(req.getTitle());
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
        roadmap.setUpdated_by(user.getUsername());

        //Cập nhật mô tả nếu có
        if (req.getDescription() != null){
            roadmap.setDescription(req.getDescription());
        }

        //Cập nhật thông tin lộ trình xuống CSDL
        try{
            roadmapRepository.save(roadmap);
            return ResponseEntity.ok(ApiResponse.success("Cập nhật lộ trình thành công", roadmap));
        }catch (Exception e){
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Lỗi trong quá trình cập nhật lộ trình : " + e));
        }
    }

    //Cập nhật trạng thái
    public ResponseEntity<ApiResponse<Roadmap>> updateStatus(String id){
        Optional<Roadmap> optRoadmap = roadmapRepository.findById(id);
        if (optRoadmap.isEmpty()){
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Không tìm thấy lộ trình tương ứng"));
        }
        Roadmap roadmap = optRoadmap.get();
        if (Objects.equals(roadmap.getStatus(), RoadmapStatus.active)){
            roadmap.setStatus(RoadmapStatus.inactive);
        }else{
            roadmap.setStatus(RoadmapStatus.active);
        }
        roadmapRepository.save(roadmap);
        return ResponseEntity.ok(ApiResponse.success("Cập nhật trạng thái thành công",roadmap));
    }

    //Lấy dữ liệu tất cả lộ trình
    public ResponseEntity<ApiResponse<List<Roadmap>>> getAllRoadmap() {
        return ResponseEntity.ok(ApiResponse.success("Lấy dữ liệu tất cả lộ trình thành công", roadmapRepository.findAll()));
    }
}
