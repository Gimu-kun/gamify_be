package com.example.gamify_be.Entity;

import com.example.gamify_be.Enums.Lesson.LessonStatusEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "lessons")
public class Lesson {
    @Id
    private final String id = UUID.randomUUID().toString();
    @Column(name = "checkpoint_id")
    private String checkPointId;
    @Column(name = "chapter_id")
    private Integer chapterId;
    private String title;
    private String content;
    private String img;
    private LessonStatusEnum status = LessonStatusEnum.active;
    @Column(name = "created_by")
    private String createdBy;
    @Column(name = "updated_by")
    private String updatedBy;

    public Lesson(){}

    public Lesson( Integer chapterId, String title, String content, String img, String operator) {
        this.chapterId = chapterId;
        this.title = title;
        this.content = content;
        this.img = img;
        createdBy = operator;
        updatedBy = operator;
    }

    public String getId() {
        return id;
    }

    public String getCheckPointId() {
        return checkPointId;
    }

    public void setCheckPointId(String checkPointId) {
        this.checkPointId = checkPointId;
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

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public LessonStatusEnum getStatus() {
        return status;
    }

    public void setStatus(LessonStatusEnum status) {
        this.status = status;
    }
}
