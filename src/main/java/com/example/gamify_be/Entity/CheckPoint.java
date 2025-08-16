package com.example.gamify_be.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "checkpoints")
public class CheckPoint {

    @Id
    private final String id = UUID.randomUUID().toString();
    @Column(name = "roadmap_id")
    private String roadmapId;
    private Integer section;
    private Integer ord;
    private String name;
    private String description;
    private String created_by;
    private String updated_by;

    public CheckPoint(){}

    public CheckPoint(  String name, String description, String operator) {
        this.name = name;
        this.description = description;
        created_by = operator;
        updated_by = operator;
    }

    public String getId() {
        return id;
    }

    public String getJourney_id() {
        return roadmapId;
    }

    public void setJourney_id(String journey_id) {
        this.roadmapId = journey_id;
    }

    public Integer getSection() {
        return section;
    }

    public void setSection(Integer section) {
        this.section = section;
    }

    public Integer getOrd() {
        return ord;
    }

    public void setOrd(Integer ord) {
        this.ord = ord;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public String getUpdated_by() {
        return updated_by;
    }

    public void setUpdated_by(String updated_by) {
        this.updated_by = updated_by;
    }
}
