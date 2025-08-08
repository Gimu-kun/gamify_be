package com.example.gamify_be.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "badges")
public class Badge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String image;
    @Column(name = "badge_level")
    private Integer badgeLevel;

    public Badge(){}

    public Badge(String name, String image, Integer badge_level) {
        this.name = name;
        this.image = image;
        this.badgeLevel = badge_level;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getBadge_level() {
        return badgeLevel;
    }

    public void setBadge_level(Integer badge_level) {
        this.badgeLevel = badge_level;
    }
}
