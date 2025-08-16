package com.example.gamify_be.Repository;

import com.example.gamify_be.Entity.TakeRoadmap;
import com.example.gamify_be.Entity.id.TakeRoadmapId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TakeRoadmapRepository extends JpaRepository<TakeRoadmap, TakeRoadmapId> {
    List<TakeRoadmap> findByRoadmapId(String roadmapId);

    List<TakeRoadmap> findByUserId(String userId);
}
