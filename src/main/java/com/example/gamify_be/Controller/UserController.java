package com.example.gamify_be.Controller;

import com.example.gamify_be.Dto.ApiResponse.ApiResponse;
import com.example.gamify_be.Dto.User.UserCreationRequestDto;
import com.example.gamify_be.Dto.User.UserLoginDto;
import com.example.gamify_be.Dto.User.UserUpdateRequestDto;
import com.example.gamify_be.Entity.User;
import com.example.gamify_be.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<ApiResponse<List<User>>> getAllUsers(){
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> findUserById(@PathVariable String id){
        return userService.findUserById(id);
    }

    @PostMapping()
    public ResponseEntity<ApiResponse<?>> userEnroll(@RequestBody UserCreationRequestDto req){
        return userService.userEnroll(req);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> userUpdate(@PathVariable String id ,@RequestBody UserUpdateRequestDto req){
        return userService.userUpdate(id,req);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<?>> userLogin(@RequestBody UserLoginDto req){
        return userService.userLogin(req);
    }

    @GetMapping("/decode/{token}")
    public ResponseEntity<ApiResponse<String>> decodeToken(@PathVariable String token){
        return userService.decodeToken(token);
    }

    @PostMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> increaseExp(@PathVariable String id,@RequestParam String exp){
        return userService.increaseExp(id,Integer.parseInt(exp));
    };
}
