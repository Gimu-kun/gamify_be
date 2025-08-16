package com.example.gamify_be.Entity;

import com.example.gamify_be.Enums.Roadmap.RoadmapStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "roadmaps")
public class Roadmap {
    @Id
    private final String id = "RM-"+UUID.randomUUID().toString().replace("-","").substring(0,12);
    private Integer ord = null;
    private String title;
    private String description = null;
    @Enumerated(EnumType.STRING)
    private RoadmapStatus status = RoadmapStatus.inactive;
    private Integer sec_count = 0;
    private Integer cp_count = 0;
    private String updated_by;
    private String created_by;
    @Column(insertable = false, updatable = false)
    private LocalDateTime created_at;
    @Column(insertable = false, updatable = false)
    private LocalDateTime updated_at;

    public Roadmap(){}

    public Roadmap(String title, String description, String operator) {
        this.title = title;
        this.description = description;
        updated_by = operator;
        created_by = operator;
    }

    public String getId() {
        return id;
    }

    public Integer getOrd() {
        return ord;
    }

    public void setOrd(Integer ord) {
        this.ord = ord;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public RoadmapStatus getStatus() {
        return status;
    }

    public void setStatus(RoadmapStatus status) {
        this.status = status;
    }

    public Integer getSec_count() {
        return sec_count;
    }

    public void setSec_count(Integer sec_count) {
        this.sec_count = sec_count;
    }

    public Integer getCp_count() {
        return cp_count;
    }

    public void setCp_count(Integer cp_count) {
        this.cp_count = cp_count;
    }

    public String getUpdated_by() {
        return updated_by;
    }

    public void setUpdated_by(String updated_by) {
        this.updated_by = updated_by;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }

    public LocalDateTime getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(LocalDateTime updated_at) {
        this.updated_at = updated_at;
    }

    @Override
    public String toString() {
        return "Roadmap{" +
                "id='" + id + '\'' +
                ", ord=" + ord +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", sec_count=" + sec_count +
                ", cp_count=" + cp_count +
                ", updated_by='" + updated_by + '\'' +
                ", created_by='" + created_by + '\'' +
                ", created_at=" + created_at +
                ", updated_at=" + updated_at +
                '}';
    }
}
