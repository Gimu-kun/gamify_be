package com.example.gamify_be.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "journeys")
public class Journey {
    @Id
    private final String id = UUID.randomUUID().toString();
    private Integer ord = null;
    private String title;
    private String description = null;
    private String updated_by;
    private String created_by;

    public Journey(){}

    public Journey(String title, String description, String operator) {
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
}
