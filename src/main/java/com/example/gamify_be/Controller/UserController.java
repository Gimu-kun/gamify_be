package com.example.gamify_be.Controller;

import com.example.gamify_be.Dto.ApiResponse.ApiResponse;
import com.example.gamify_be.Dto.User.UserCreationRequestDto;
import com.example.gamify_be.Dto.User.UserLoginDto;
import com.example.gamify_be.Dto.User.UserUpdateRequestDto;
import com.example.gamify_be.Entity.User;
import com.example.gamify_be.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/user")
@CrossOrigin("*")
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping
    public ApiResponse<List<User>> getAllUsers(){
        return ApiResponse.success("Lấy thông tin thành công",userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ApiResponse<?> getUserById(@PathVariable String id){
        User user = userService.getUserById(id);
        if (user != null) {
            return ApiResponse.success("Đã tìm thấy người dùng",user);
        }else{
            return ApiResponse.error("Không tìm thấy người dùng với id " + id,HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping()
    public ApiResponse<?> userEnroll(@RequestBody UserCreationRequestDto req){
        return userService.userEnroll(req);
    }

    @PatchMapping("/{id}")
    public ApiResponse<?> userUpdate(@PathVariable String id ,@RequestBody UserUpdateRequestDto req){
        return userService.userUpdate(id,req);
    }

    @PostMapping("/login")
    public ApiResponse<?> userLogin(@RequestBody UserLoginDto req){
        return userService.userLogin(req);
    }

    @GetMapping("/decode/{token}")
    public ApiResponse<?> decodeToken(@PathVariable String token){
        return userService.decodeToken(token);
    }
}
