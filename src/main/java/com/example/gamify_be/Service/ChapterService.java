package com.example.gamify_be.Service;

import com.example.gamify_be.Dto.ApiResponse.ApiResponse;
import com.example.gamify_be.Entity.Chapter;
import com.example.gamify_be.Repository.ChapterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChapterService {
    @Autowired
    ChapterRepository chapterRepository;

    //Kiểm tra tồn tại bằng ID chương
    public boolean isExistByID(Integer id){
        return chapterRepository.findById(id).isPresent();
    }

    //Lấy thông tin toàn bộ chương
    public ResponseEntity<ApiResponse<List<Chapter>>> getAllChapter(){
        return ResponseEntity.ok(ApiResponse.success("Lấy thông tin chương thành công",chapterRepository.findAll()));
    }

    //Lấy tên hiển thị dựa theo id
    public String getDisplayNameById(Integer id){
        Optional<Chapter> chapter = chapterRepository.findById(id);
        return chapter.map(Chapter::getDisplayName).orElse(null);
    }

    //Lấy id dựa trên tên hiển thị
    public Integer getIdByDisplayName(String displayName){
        Optional<Chapter> chapter = chapterRepository.findByDisplayName(displayName);
        return chapter.map(Chapter::getId).orElse(null);
    }
}
