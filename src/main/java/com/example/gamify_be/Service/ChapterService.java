package com.example.gamify_be.Service;

import com.example.gamify_be.Entity.Chapter;
import com.example.gamify_be.Repository.ChapterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ChapterService {
    @Autowired
    ChapterRepository chapterRepository;

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
