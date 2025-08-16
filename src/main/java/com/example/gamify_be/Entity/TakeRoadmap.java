package com.example.gamify_be.Entity;

import com.example.gamify_be.Entity.id.TakeRoadmapId;
import jakarta.persistence.*;

@Entity
@Table(name = "take_roadmaps")
public class TakeRoadmap {
    @EmbeddedId
    private TakeRoadmapId id = new TakeRoadmapId();

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @MapsId("roadmapId")
    @JoinColumn(name = "roadmap_id")
    private Roadmap roadmap;

    public TakeRoadmap(){}

    public TakeRoadmap(User user, Roadmap roadmap) {
        this.user = user;
        this.roadmap = roadmap;
        id = new TakeRoadmapId(user.getId(), roadmap.getId());
    }

    public TakeRoadmapId getId() {
        return id;
    }

    public void setId(TakeRoadmapId id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Roadmap getroadmap() {
        return roadmap;
    }

    public void setroadmap(Roadmap roadmap) {
        this.roadmap = roadmap;
    }
}
