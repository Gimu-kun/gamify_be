package com.example.gamify_be.Controller;

import com.example.gamify_be.Dto.ApiResponse.ApiResponse;
import com.example.gamify_be.Entity.Chapter;
import com.example.gamify_be.Service.ChapterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/chapter")
@CrossOrigin("*")
public class ChapterController {
    @Autowired
    ChapterService chapterService;

    @GetMapping()
    public ResponseEntity<ApiResponse<List<Chapter>>> getAllChapter(){
        return chapterService.getAllChapter();
    }
}
