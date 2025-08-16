package com.example.gamify_be.Entity.id;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class TakeRoadmapId implements Serializable {
    @Column(name = "user_id")
    private String userId;
    @Column(name = "roadmap_id")
    private String roadmapId;

    public TakeRoadmapId(){}

    public TakeRoadmapId(String userId, String roadmapId) {
        this.userId = userId;
        this.roadmapId = roadmapId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getroadmapId() {
        return roadmapId;
    }

    public void setroadmapId(String roadmapId) {
        this.roadmapId = roadmapId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TakeRoadmapId that)) return false;
        return (Objects.equals(userId,that.userId)) && (Objects.equals(roadmapId,that.roadmapId));
    }

    @Override
    public int hashCode(){
        return Objects.hash(userId,roadmapId);
    }
}
