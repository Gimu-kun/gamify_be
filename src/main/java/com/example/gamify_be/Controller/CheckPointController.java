package com.example.gamify_be.Controller;

import com.example.gamify_be.Dto.ApiResponse.ApiResponse;
import com.example.gamify_be.Dto.CheckPoint.CPRequestDto;
import com.example.gamify_be.Dto.CheckPoint.CPPositionRequestDto;
import com.example.gamify_be.Entity.CheckPoint;
import com.example.gamify_be.Service.CheckPointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/check_point")
@CrossOrigin("*")
public class CheckPointController {
    @Autowired
    CheckPointService checkPointService;

    @PostMapping()
    public ApiResponse<CheckPoint> createCP(@RequestBody CPRequestDto req){
        return checkPointService.createCP(req);
    }

    @PatchMapping("/position/{id}")
    public ApiResponse<CheckPoint> setPosition(@PathVariable String id,
                                               @RequestBody CPPositionRequestDto req){
        return checkPointService.setPosition(id,req);
    }

    @PatchMapping("/ord/{id}")
    public ApiResponse<CheckPoint> setCPOrder(@PathVariable String id,
                                              @RequestParam Integer order,
                                              @RequestParam String operator){
        return checkPointService.setCPOrder(id,order,operator);
    }

    @PatchMapping("/rm_ord/{id}")
    public ApiResponse<CheckPoint> removeCPOrder(@PathVariable String id,
                                                 @RequestParam String operator){
        return checkPointService.removeCPOrder(id,operator);
    }

    @PatchMapping("/update/{id}")
    public ApiResponse<CheckPoint> updateCP(@PathVariable String id,
                                            @RequestBody CPRequestDto req){
        return checkPointService.updateCP(id,req);
    }
}
