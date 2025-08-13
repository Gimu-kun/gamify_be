package com.example.gamify_be.Controller;

import com.example.gamify_be.Dto.ApiResponse.ApiResponse;
import com.example.gamify_be.Dto.Lesson.LessonRequestDto;
import com.example.gamify_be.Entity.Lesson;
import com.example.gamify_be.Service.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api/lesson")
@CrossOrigin("*")
public class LessonController {
    @Autowired
    LessonService lessonService;

    @GetMapping()
    public ResponseEntity<ApiResponse<List<Lesson>>> getAllLesson(){
        return lessonService.getAllLesson();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Lesson>> getLessonById(@PathVariable String id){
        return lessonService.getLessonById(id);
    }

    @GetMapping("/by_cp/{checkPointId}")
    public ResponseEntity<ApiResponse<List<Lesson>>> getLessonByCPId(@PathVariable String checkPointId){
        return lessonService.getLessonByCPId(checkPointId);
    }

    @PostMapping()
    public ResponseEntity<ApiResponse<Lesson>> createLesson(
            @ModelAttribute LessonRequestDto req,
            @RequestParam(required = false) MultipartFile image) throws IOException {
        return lessonService.createLesson(req,image);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<Lesson>> updateLesson(
            @PathVariable String id,
            @ModelAttribute LessonRequestDto req,
            @RequestParam(required = false) MultipartFile image){
        return lessonService.updateLesson(id,req,image);
    }

    @PatchMapping("mv_in_cp")
    public ResponseEntity<ApiResponse<Lesson>> setLessonToCP(
            @RequestParam String id,
            @RequestParam String cp_id
    ){
        return lessonService.setLessonToCP(id,cp_id);
    }

    @PatchMapping("rm_from_cp")
    public ResponseEntity<ApiResponse<Lesson>> removeLessonFromCP(
            @RequestParam String id
    ){
        return lessonService.removeLessonFromCP(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> deleteLesson(
            @PathVariable String id
    ){
        return lessonService.deleteLesson(id);
    }
}
