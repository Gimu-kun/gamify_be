package com.example.gamify_be.Controller;

import com.example.gamify_be.Dto.ApiResponse.ApiResponse;
import com.example.gamify_be.Dto.Journey.JourneyRequestDto;
import com.example.gamify_be.Entity.Journey;
import com.example.gamify_be.Service.JourneyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/journey")
@CrossOrigin("*")
public class JourneyController {
    @Autowired
    JourneyService journeyService;

    @PostMapping()
    public ApiResponse<Journey> createJourney(@RequestBody JourneyRequestDto req){
        return journeyService.createJourney(req);
    }

    @PatchMapping("/ord/{id}")
    public ApiResponse<Journey> setOrder(@PathVariable String id,@RequestParam Integer order){
        return journeyService.setOrder(id,order);
    }

    @PatchMapping("/{id}")
    public ApiResponse<Journey> updateJourney(@PathVariable String id,@RequestBody JourneyRequestDto req)
    {
        return journeyService.updateJourney(id,req);
    }
}
