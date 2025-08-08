package com.example.gamify_be.Repository;

import com.example.gamify_be.Entity.Badge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BadgeRepository extends JpaRepository<Badge,Integer> {

    Optional<Badge> findBadgeByBadgeLevel(Integer level);
}
