package com.example.gamify_be.Repository;

import com.example.gamify_be.Entity.CheckIn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CheckInRepository extends JpaRepository<CheckIn,Integer> {
    Optional<CheckIn> findCheckInByUserIdAndCheckInDate(String id, LocalDate checkInDate);

    List<CheckIn> findAllByUserId(String userId);
}
