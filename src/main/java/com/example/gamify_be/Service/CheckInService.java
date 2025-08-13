package com.example.gamify_be.Service;

import com.example.gamify_be.Dto.ApiResponse.ApiResponse;
import com.example.gamify_be.Entity.CheckIn;
import com.example.gamify_be.Repository.CheckInRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class CheckInService {
    @Autowired
    CheckInRepository checkInRepository;

    //Hàm lấy dữ liệu check in bằng id người dùng và ngày check in
    private CheckIn getCheckInByDate(String userId, LocalDate checkInDate){
        Optional<CheckIn> checkIn = checkInRepository.findCheckInByUserIdAndCheckInDate(userId,checkInDate);
        return checkIn.orElse(null);
    };

    //Lấy bảng check in từ id người dùng
    private List<CheckIn> getAllCheckInByUserId(String userId){
        return checkInRepository.findAllByUserId(userId);
    }

    public ResponseEntity<ApiResponse<CheckIn>> checkIn(String id, LocalDate checkInDate, LocalTime checkInTime){
        //Kiểm tra hnay đã điểm danh chưa
        CheckIn checkInToday = getCheckInByDate(id, checkInDate);
        //Hôm nay đã điểm danh
        if (checkInToday != null){
            checkInToday.setCheckInTime(checkInTime);
            checkInRepository.save(checkInToday);
            return ResponseEntity.ok(ApiResponse.success("Hôm nay đã điểm danh, thời gian điểm danh đã cập nhật",checkInToday));
        }
        //Hôm nay chưa điểm danh
        CheckIn checkInYesterday = getCheckInByDate(id, checkInDate.minusDays(1));
        //Tạo điểm danh mới
        CheckIn nCheck = new CheckIn(id,checkInDate,checkInTime);
        //Nếu hôm qua có điểm danh
        if (checkInYesterday != null){
            nCheck.setStreakCount(checkInYesterday.getStreakCount()+1);
        }
        checkInRepository.save(nCheck);
        return ResponseEntity.ok(ApiResponse.success("Đã điểm danh thành công",nCheck));
    }

    public ResponseEntity<ApiResponse<Integer>> getMaxStreak(String userId){
        //Lấy danh sách theo id người dùng
        List<CheckIn> checkIns = getAllCheckInByUserId(userId);
        //Người dùng chưa từng điểm danh
        if (checkIns.isEmpty()){
            return ResponseEntity.ok(ApiResponse.success("Lấy streak tối đa thành công",0));
        }
        //Tìm streak tối đa
        Integer maxStreak = 1;
        for(CheckIn ci : checkIns){
            if (ci.getStreakCount() > maxStreak){
                maxStreak = ci.getStreakCount();
            }
        }
        //Trả về streak tối đa cho front-end
        return ResponseEntity.ok(ApiResponse.success("Lấy streak tối đa thành công",maxStreak));
    }

    public ResponseEntity<ApiResponse<Integer>> getCurrentStreak(String userId, LocalDate checkInDate){
        //Lấy dữ liệu check in bằng id người dùng và ngày check in (ngày hiện tại)
        CheckIn checkIn = getCheckInByDate(userId,checkInDate);
        //Nếu hnay chưa check in trả về 0
        if (checkIn == null){
            return ResponseEntity.ok(ApiResponse.success("Lấy streak hiện tại thành công",0));
        }
        //Đã check in trả về streak count của ngày hnay
        return ResponseEntity.ok(ApiResponse.success("Lấy streak hiện tại thành công",checkIn.getStreakCount()));
    }
}
