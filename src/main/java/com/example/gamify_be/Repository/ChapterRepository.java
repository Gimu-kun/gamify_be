package com.example.gamify_be.Repository;

import com.example.gamify_be.Entity.Chapter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChapterRepository extends JpaRepository<Chapter,Integer> {
    Optional<Chapter> findByDisplayName(String displayName);
}
