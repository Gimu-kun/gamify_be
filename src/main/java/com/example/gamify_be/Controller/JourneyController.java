package com.example.gamify_be.Controller;

import com.example.gamify_be.Dto.ApiResponse.ApiResponse;
import com.example.gamify_be.Dto.Journey.JourneyRequestDto;
import com.example.gamify_be.Entity.Journey;
import com.example.gamify_be.Entity.User;
import com.example.gamify_be.Service.JourneyService;
import com.example.gamify_be.Service.TakeJourneyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/journey")
@CrossOrigin("*")
public class JourneyController {
    @Autowired
    JourneyService journeyService;
    @Autowired
    TakeJourneyService takeJourneyService;

    @PostMapping()
    public ResponseEntity<ApiResponse<Journey>> createJourney(@RequestBody JourneyRequestDto req){
        return journeyService.createJourney(req);
    }

    @PatchMapping("/ord/{id}")
    public ResponseEntity<ApiResponse<Journey>> setOrder(@PathVariable String id,@RequestParam Integer order){
        return journeyService.setOrder(id,order);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<Journey>> updateJourney(@PathVariable String id,@RequestBody JourneyRequestDto req)
    {
        return journeyService.updateJourney(id,req);
    }

    @PostMapping("/take")
    public ResponseEntity<ApiResponse<?>> takeJourney(@RequestParam String userId,@RequestParam String journeyId){
        return takeJourneyService.createTakeJourney(userId,journeyId);
    }

    @GetMapping("/taken_users")
    public ResponseEntity<ApiResponse<List<User>>> getAllTakenUserByJourney(@RequestParam String journeyId){
        return takeJourneyService.getAllTakenUserByJourney(journeyId);
    }

    @GetMapping("/taken_journeys")
    public ResponseEntity<ApiResponse<List<Journey>>> getAllTakenJourneyByUser(@RequestParam String userId){
        return takeJourneyService.getAllTakenJourneyByUser(userId);
    }
}
