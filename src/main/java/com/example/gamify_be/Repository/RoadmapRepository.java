package com.example.gamify_be.Repository;

import com.example.gamify_be.Entity.Roadmap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoadmapRepository extends JpaRepository<Roadmap,String> {
    Optional<Roadmap> findByTitle(String title);

    Optional<Roadmap> findByOrd(Integer ord);
}
