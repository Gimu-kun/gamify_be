package com.example.gamify_be.Entity;

import com.example.gamify_be.Entity.id.TakeJourneyId;
import jakarta.persistence.*;

@Entity
@Table(name = "take_journeys")
public class TakeJourney {
    @EmbeddedId
    private TakeJourneyId id = new TakeJourneyId();

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @MapsId("journeyId")
    @JoinColumn(name = "journey_id")
    private Journey journey;

    public TakeJourney(){}

    public TakeJourney(User user, Journey journey) {
        this.user = user;
        this.journey = journey;
        id = new TakeJourneyId(user.getId(), journey.getId());
    }

    public TakeJourneyId getId() {
        return id;
    }

    public void setId(TakeJourneyId id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Journey getJourney() {
        return journey;
    }

    public void setJourney(Journey journey) {
        this.journey = journey;
    }
}
