package com.example.gamify_be.Repository;

import com.example.gamify_be.Entity.Journey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JourneyRepository extends JpaRepository<Journey,String> {
    Optional<Journey> findByTitle(String title);

    Optional<Journey> findByOrd(Integer ord);
}
