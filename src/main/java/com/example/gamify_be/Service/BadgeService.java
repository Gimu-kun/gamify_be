package com.example.gamify_be.Service;

import com.example.gamify_be.Entity.Badge;
import com.example.gamify_be.Repository.BadgeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;

@Service
public class BadgeService {
    @Autowired
    BadgeRepository badgeRepository;

    public Integer getBadgeIdByLevel(Integer level){
        Optional<Badge> badge = badgeRepository.findBadgeByBadgeLevel(level);
        return badge.map(Badge::getId).orElse(null);
    };

    public Integer getLevelById(Integer id){
        Optional<Badge> badge = badgeRepository.findById(id);
        return badge.map(Badge::getBadge_level).orElse(null);
    };

    public boolean isUpgradeBadge(Integer currentExp, Integer currentBadgeLevel){
        Integer[] badgeMark = {0,2000,5000,9000,14000,20000};
        if (currentBadgeLevel == 6){
            return false;
        }
        return currentExp >= badgeMark[currentBadgeLevel];
    }
}
