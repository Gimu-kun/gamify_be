package com.example.gamify_be.Service;

import com.example.gamify_be.Entity.Badge;
import com.example.gamify_be.Repository.BadgeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BadgeService {
    @Autowired
    BadgeRepository badgeRepository;

    //Hàm tìm id của huy hiệu bằng cấp độ huy hiệu
    public Integer getBadgeIdByLevel(Integer level){
        Optional<Badge> badge = badgeRepository.findBadgeByBadgeLevel(level);
        return badge.map(Badge::getId).orElse(null);
    };

    //Hàm tìm cấp độ huy hiệu bằng id của huy hiệu
    public Integer getLevelById(Integer id){
        Optional<Badge> badge = badgeRepository.findById(id);
        return badge.map(Badge::getBadge_level).orElse(null);
    };

    //Xác định kinh nghiệm đã đạt móc để nâng cấp huy hiệu chưa
    public boolean isUpgradeBadge(Integer currentExp, Integer currentBadgeLevel){
        //Các móc kinh nghiệm để nâng cấp huy hiệu
        Integer[] badgeMark = {0,2000,5000,9000,14000,20000};

        //Nếu như đạt móc tối đa thì trả về false không nâng cấp nữa
        if (currentBadgeLevel == 6){
            return false;
        }

        //Nếu đạt móc kế tiếp thì trả về true nâng cấp huy hiệu
        return currentExp >= badgeMark[currentBadgeLevel];
    }
}
