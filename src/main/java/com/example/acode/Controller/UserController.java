package com.example.acode.Controller;

import com.example.acode.DTO.User.UserCreationRequestDto;
import com.example.acode.DTO.User.UserLoginRequestDto;
import com.example.acode.DTO.User.UserUpdateRequestDto;
import com.example.acode.Entity.User;
import com.example.acode.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/user")
@CrossOrigin("*")
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(){
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable String id) {
        try {
            Optional<User> user = userService.getUserById(id);
            return user.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid UUID format: " + id);
        }
    }


    @PostMapping()
    public ResponseEntity<?> createUser(@RequestBody UserCreationRequestDto request){
        try{
            return userService.createUser(request);
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi" + e);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> userLogin(@RequestBody UserLoginRequestDto request){
        return userService.userLogin(request);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable String id, @RequestBody UserUpdateRequestDto request){
        try{
            return userService.updateUser(id, request);
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi" + e);
        }
    }

    @PatchMapping("/exp/{id}")
    public ResponseEntity<?> increaseExp(@PathVariable String id, @RequestParam Integer exp){
        return userService.increaseExp(id,exp);
    }
}
