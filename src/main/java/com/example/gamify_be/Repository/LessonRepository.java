package com.example.gamify_be.Repository;

import com.example.gamify_be.Entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LessonRepository extends JpaRepository<Lesson,String> {
    List<Lesson> findAllByCheckPointId(String checkPointId);

    Optional<Object> findByTitle(String title);
}
