package com.example.gamify_be.Repository;

import com.example.gamify_be.Entity.TakeJourney;
import com.example.gamify_be.Entity.id.TakeJourneyId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TakeJourneyRepository extends JpaRepository<TakeJourney, TakeJourneyId> {
    List<TakeJourney> findByJourneyId(String journeyId);

    List<TakeJourney> findByUserId(String userId);
}
