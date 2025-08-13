package com.example.gamify_be.Controller;

import com.example.gamify_be.Dto.ApiResponse.ApiResponse;
import com.example.gamify_be.Dto.CheckIn.CheckInRequestDto;
import com.example.gamify_be.Entity.CheckIn;
import com.example.gamify_be.Service.CheckInService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("api/check_in")
@CrossOrigin("*")
public class CheckInController {
    @Autowired
    CheckInService checkInService;

    @PostMapping("/{id}")
    public ResponseEntity<ApiResponse<CheckIn>> checkIn(@PathVariable String id, @RequestBody CheckInRequestDto req){
        return checkInService.checkIn(id,req.getCheckInDate(),req.getCheckInTime());
    }

    @GetMapping("/max_streak/{userId}")
    public ResponseEntity<ApiResponse<Integer>> getMaxStreak(@PathVariable String userId){
        return checkInService.getMaxStreak(userId);
    }

    @GetMapping("/current_streak/{userId}")
    public ResponseEntity<ApiResponse<Integer>> getCurrentStreak(@PathVariable String userId, @RequestParam LocalDate checkInDate){
        return checkInService.getCurrentStreak(userId,checkInDate);
    }
}
