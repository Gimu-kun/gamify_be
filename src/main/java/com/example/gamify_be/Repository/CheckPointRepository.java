package com.example.gamify_be.Repository;

import com.example.gamify_be.Entity.CheckPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CheckPointRepository extends JpaRepository<CheckPoint,String> {
    List<CheckPoint> findAllByRoadmapIdAndSection(String journeyId, Integer section);

    Optional<CheckPoint> findByName(String name);
}
