package com.example.gamify_be.Entity;

import jakarta.persistence.*;

@Entity
public class Chapter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "display_name")
    private String displayName;
    private String created_by;
    private String updated_by;

    public Chapter(String displayName, String operator) {
        this.displayName = displayName;
        created_by = operator;
        updated_by = operator;
    }

    public Integer getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
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
