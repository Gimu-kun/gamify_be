package com.example.gamify_be.Entity.id;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class TakeJourneyId implements Serializable {
    @Column(name = "user_id")
    private String userId;
    @Column(name = "journey_id")
    private String journeyId;

    public TakeJourneyId(){}

    public TakeJourneyId(String userId, String journeyId) {
        this.userId = userId;
        this.journeyId = journeyId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getJourneyId() {
        return journeyId;
    }

    public void setJourneyId(String journeyId) {
        this.journeyId = journeyId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TakeJourneyId that)) return false;
        return (Objects.equals(userId,that.userId)) && (Objects.equals(journeyId,that.journeyId));
    }

    @Override
    public int hashCode(){
        return Objects.hash(userId,journeyId);
    }
}
