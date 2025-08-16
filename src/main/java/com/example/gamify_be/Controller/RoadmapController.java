package com.example.gamify_be.Controller;

import com.example.gamify_be.Dto.ApiResponse.ApiResponse;
import com.example.gamify_be.Dto.Roadmap.RoadmapRequestDto;
import com.example.gamify_be.Entity.Roadmap;
import com.example.gamify_be.Entity.User;
import com.example.gamify_be.Service.RoadmapService;
import com.example.gamify_be.Service.TakeRoadmapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/roadmap")
@CrossOrigin("*")
public class RoadmapController {
    @Autowired
    RoadmapService roadmapService;
    @Autowired
    TakeRoadmapService takeRoadmapService;

    @GetMapping()
    public ResponseEntity<ApiResponse<List<Roadmap>>> getAllRoadmap(){
        return roadmapService.getAllRoadmap();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Roadmap>> getRoadmapById(@PathVariable String id){
        Roadmap roadmap = roadmapService.getRoadmapById(id);
        if (roadmap == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error("Không tìm thấy dữ liệu của lộ trình"));
        }else{
            return ResponseEntity.ok(ApiResponse.success("Lấy dữ liệu của lộ trình thành công", roadmap));
        }
    }

    @PostMapping()
    public ResponseEntity<ApiResponse<Roadmap>> createRoadmap(@RequestBody RoadmapRequestDto req){
        return roadmapService.createRoadmap(req);
    }

    @PatchMapping("/ord/{id}")
    public ResponseEntity<ApiResponse<Roadmap>> setOrder(@PathVariable String id, @RequestParam Integer order){
        return roadmapService.setOrder(id,order);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<Roadmap>> updateRoadmap(@PathVariable String id, @RequestBody RoadmapRequestDto req) {
        return roadmapService.updateRoadmap(id,req);
    }

    @PatchMapping("/status/{id}")
    public ResponseEntity<ApiResponse<Roadmap>> updateStatus(@PathVariable String id){
        return roadmapService.updateStatus(id);
    }

    @PostMapping("/take")
    public ResponseEntity<ApiResponse<?>> takeRoadmap(@RequestParam String userId,@RequestParam String RoadmapId){
        return takeRoadmapService.createTakeRoadmap(userId,RoadmapId);
    }

    @GetMapping("/taken_users")
    public ResponseEntity<ApiResponse<List<User>>> getAllTakenUserByRoadmap(@RequestParam String RoadmapId){
        return takeRoadmapService.getAllTakenUserByRoadmap(RoadmapId);
    }

    @GetMapping("/taken_Roadmaps")
    public ResponseEntity<ApiResponse<List<Roadmap>>> getAllTakenRoadmapByUser(@RequestParam String userId){
        return takeRoadmapService.getAllTakenRoadmapByUser(userId);
    }
}
