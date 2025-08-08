package com.example.gamify_be.Service;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.gamify_be.Dto.ApiResponse.ApiResponse;
import com.example.gamify_be.Dto.User.ExpUpgradeResponse;
import com.example.gamify_be.Dto.User.UserCreationRequestDto;
import com.example.gamify_be.Dto.User.UserLoginDto;
import com.example.gamify_be.Dto.User.UserUpdateRequestDto;
import com.example.gamify_be.Entity.User;
import com.example.gamify_be.Repository.UserRepository;
import com.example.gamify_be.Utils.JwtUtil;
import com.example.gamify_be.Utils.PasswordsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    BadgeService badgeService;

    private boolean isUsernameExisted(String username){
        Optional<User> user = userRepository.findByUsername(username);
        return user.isPresent();
    };

    private User getUserByUsername(String username){
        Optional<User> user = userRepository.findByUsername(username);
        return user.orElse(null);
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    };

    public User getUserById(String id){
        Optional<User> user = userRepository.findById(id);
        return user.orElse(null);
    }

    public ApiResponse<?> userEnroll(UserCreationRequestDto req){
        System.out.println(req.toString());
        //Validate before save to DB
        if (req.getUsername() == null || req.getUsername().isBlank()){
            return ApiResponse.error("Tên đăng nhập không được để trống",HttpStatus.BAD_REQUEST);
        }

        if (req.getUsername().length() < 8 ){
            return ApiResponse.error("Tên tài khoản không được ít hơn 8 kí tự",HttpStatus.BAD_REQUEST);
        }

        if (isUsernameExisted(req.getUsername())){
            return ApiResponse.error("Tên tài khoản đã tồn tại!",HttpStatus.BAD_REQUEST);
        }

        if (req.getPasswords() == null || req.getPasswords().isBlank()){
            return ApiResponse.error("Mật khẩu không được để trống",HttpStatus.BAD_REQUEST);
        }

        if (req.getPasswords().length() < 8 ){
            return ApiResponse.error("Mật khẩu không được ít hơn 8 kí tự",HttpStatus.BAD_REQUEST);
        }

        if (req.getGender() == null){
            return ApiResponse.error("Mời chọn giới tính",HttpStatus.BAD_REQUEST);
        }

        if (req.getDob() == null){
            return ApiResponse.error("Mời nhập ngày sinh",HttpStatus.BAD_REQUEST);
        }

        if (req.getFull_name() == null || req.getFull_name().isBlank()){
            return ApiResponse.error("Họ và tên không được để trống",HttpStatus.BAD_REQUEST);
        }

        //Save to DB
        try{
            User nUser = new User(
                    req.getFull_name(),
                    req.getUsername(),
                    PasswordsUtil.hashPassword(req.getPasswords()),
                    req.getGender(),
                    req.getDob());
            userRepository.save(nUser);
            return ApiResponse.success("Đăng ký thành công, chúc bạn có một ngày học vui vẻ!",null);
        }catch(Exception ex){
            return ApiResponse.error("Lỗi : " + ex, HttpStatus.CONFLICT);
        }
    }

    public ApiResponse<?> userUpdate(String id, UserUpdateRequestDto req){
        //Find user
        User user = getUserById(id);
        if (user == null){
            return ApiResponse.error("Không tìm thấy người dùng tương ứng với id " + id,HttpStatus.NOT_FOUND);
        }

        //Check confirm password
        if (req.getPasswords_confirm() == null || req.getPasswords_confirm().isBlank()){
            return ApiResponse.error("Mật khẩu xác thực không được để trống",HttpStatus.BAD_REQUEST);
        }

        if (!PasswordsUtil.checkPassword(req.getPasswords_confirm(),user.getPasswords())){
            return  ApiResponse.error("Mật khẩu xác thực không đúng",HttpStatus.BAD_REQUEST);
        }

        //Validate update fields
        if(req.getFull_name() != null){
            user.setFull_name(req.getFull_name());
        }

        if (req.getPasswords() != null){
            if (req.getPasswords().length() < 8){
                return ApiResponse.error("Mật khẩu phải có ít nhất 8 ký tự", HttpStatus.BAD_REQUEST);
            }
            user.setPasswords(PasswordsUtil.hashPassword(req.getPasswords()));
        }

        if (req.getGender() != null){
            req.setGender(req.getGender());
        }

        if (req.getDob() != null){
            req.setDob(req.getDob());
        }

        try{
            userRepository.save(user);
            return ApiResponse.success("Cập nhật thành công",null);
        }catch(Exception ex){
            return ApiResponse.error("Lỗi : " + ex, HttpStatus.CONFLICT);
        }
    };

    public ApiResponse<?> userLogin(UserLoginDto req){
        if (req.getUsername() == null){
            return ApiResponse.error("Tài khoản không được để trống",HttpStatus.BAD_REQUEST);
        }

        if (req.getPasswords() == null){
            return ApiResponse.error("Mật khẩu không được để trống",HttpStatus.BAD_REQUEST);
        }

        User user = getUserByUsername(req.getUsername());
        if (user == null){
            return ApiResponse.error("Tài khoản không tồn tại",HttpStatus.NOT_FOUND);
        }

        if (!PasswordsUtil.checkPassword(req.getPasswords(),user.getPasswords())){
            return ApiResponse.error("Mật khẩu đăng nhập không đúng",HttpStatus.BAD_REQUEST);
        }

        return ApiResponse.success("Đăng nhập thành công, chúc một ngày học tập vui vẻ",jwtUtil.createToken(user.getId()));
    }

    public ApiResponse<?> decodeToken(String token){
        DecodedJWT jwt = jwtUtil.decodeToken(token);
        return ApiResponse.success("Mã token",jwt.getSubject());
    }

    public ApiResponse<?> increaseExp(String id, Integer exp){
        User user = getUserById(id);
        if (user == null){
            return ApiResponse.error("Không tìm thấy tài khoản tương ứng",HttpStatus.NOT_FOUND);
        }
        Integer currentExp = user.getExp() + exp;
        Integer currentBadgeLevel = badgeService.getLevelById(user.getBadge_id());
        user.setExp(currentExp);
        boolean upgradeFlag = false;
        if (badgeService.isUpgradeBadge(currentExp,currentBadgeLevel) && currentBadgeLevel < 6){
            Integer targetBadgeId = badgeService.getBadgeIdByLevel(currentBadgeLevel + 1);
            user.setBadge_id(targetBadgeId);
            upgradeFlag = true;
        }
        userRepository.save(user);
        ExpUpgradeResponse response = new ExpUpgradeResponse(upgradeFlag);
        return ApiResponse.success("Đã tăng " + exp + " exp",response);
    }
}
