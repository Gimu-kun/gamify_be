package com.example.gamify_be.Dto.Lesson;

import jakarta.persistence.Column;
import org.springframework.web.multipart.MultipartFile;

public class LessonCreationRequestDto {
    private Integer chapterId;
    private String title;
    private String content;
    private String operator;

    public LessonCreationRequestDto(){}

    public LessonCreationRequestDto( Integer chapterId, String title, String content, String operator) {
        this.chapterId = chapterId;
        this.title = title;
        this.content = content;
        this.operator = operator;
    }

    public Integer getChapterId() {
        return chapterId;
    }

    public void setChapterId(Integer chapterId) {
        this.chapterId = chapterId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }
}
