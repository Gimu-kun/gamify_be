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

    //Hàm kiểm tra tên tài khoản tồn tại
    private boolean isUsernameExisted(String username){
        Optional<User> user = userRepository.findByUsername(username);
        return user.isPresent();
    };

    //Hàm tìm đối tượng người dụng bằng tên tài khoản
    private User getUserByUsername(String username){
        Optional<User> user = userRepository.findByUsername(username);
        return user.orElse(null);
    }

    //Hàm truy xuất tên tài khoản bằng ID
    public String getUsernameById(String id){
        Optional<User> user = userRepository.findById(id);
        return user.map(User::getUsername).orElse(null);
    }

    //Truy xuất tất cả người dùng
    public ResponseEntity<ApiResponse<List<User>>> getAllUsers(){
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách tất cả người dùng thành công",userRepository.findAll()));
    };

    //Truy xuất người dùng bằng id
    public User getUserById(String id){
        Optional<User> user = userRepository.findById(id);
        return  user.orElse(null);
    }

    public ResponseEntity<ApiResponse<User>> findUserById(String id){
        Optional<User> user = userRepository.findById(id);
        return user.map(value -> ResponseEntity.ok(ApiResponse.success("Đã tìm thấy người dùng", value)))
                .orElseGet(() -> ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error("Không tìm thấy người dùng với id " + id)));
    }

    //Đăng ký tài khoản mới
    public ResponseEntity<ApiResponse<?>> userEnroll(UserCreationRequestDto req){

        //Xác thực dữ liệu đầu vào trước khi thêm
        //Tên tài khoản không được để trống
        if (req.getUsername() == null || req.getUsername().isBlank()){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Tên đăng nhập không được để trống"));
        }

        //Tên tài khoản phải có ít nhất 8 ký tự
        if (req.getUsername().length() < 8 ){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Tên tài khoản không được ít hơn 8 kí tự"));
        }

        //Tên tài khoản không được trùng
        if (isUsernameExisted(req.getUsername())){
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(ApiResponse.error("Tên tài khoản đã tồn tại!"));
        }

        //Mật khẩu không được để trống
        if (req.getPasswords() == null || req.getPasswords().isBlank()){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Mật khẩu không được để trống"));
        }

        //Mật khẩu phải có ít nhất 8 ký tự
        if (req.getPasswords().length() < 8 ){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Mật khẩu không được ít hơn 8 kí tự"));
        }

        //Giới tính bắc buộc
        if (req.getGender() == null){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Mời chọn giới tính"));
        }

        //Ngày sinh không được để trống
        if (req.getDob() == null){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Mời nhập ngày sinh"));
        }

        //Họ và tên không được để trống
        if (req.getFull_name() == null || req.getFull_name().isBlank()){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Họ và tên không được để trống"));
        }

        //Lưu đối tượng người dùng mới xuống CSDL
        try{
            User nUser = new User(
                    req.getFull_name(),
                    req.getUsername(),
                    PasswordsUtil.hashPassword(req.getPasswords()),
                    req.getGender(),
                    req.getDob());
            userRepository.save(nUser);
            return ResponseEntity.ok(ApiResponse.success("Đăng ký thành công, chúc bạn có một ngày học vui vẻ!",null));
        }catch(Exception ex){
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Lỗi : " + ex));
        }
    }

    //Cập nhật thông tin người dùng
    public ResponseEntity<ApiResponse<?>> userUpdate(String id, UserUpdateRequestDto req){
        //Tìm ra đối tượng người dùng
        User user = getUserById(id);

        //TH1: Không tìm thấy đối tượng người dùng
        if (user == null){
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Không tìm thấy người dùng tương ứng với id " + id));
        }

        //TH2: Tìm thấy đối tượng người dùng
        //Mật khẩu xác thực không được để trống
        if (req.getPasswords_confirm() == null || req.getPasswords_confirm().isBlank()){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Mật khẩu xác thực không được để trống"));
        }

        //Xác thực mật khẩu trước khi cập nhật dữ liệu
        if (!PasswordsUtil.checkPassword(req.getPasswords_confirm(),user.getPasswords())){
            return  ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Mật khẩu xác thực không đúng"));
        }

        //Xác thực và cập nhật các trường còn lại
        if(req.getFull_name() != null){
            user.setFull_name(req.getFull_name());
        }

        //Xác thực và cập nhật mật khẩu
        if (req.getPasswords() != null){
            if (req.getPasswords().length() < 8){
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(ApiResponse.error("Mật khẩu phải có ít nhất 8 ký tự"));
            }
            user.setPasswords(PasswordsUtil.hashPassword(req.getPasswords()));
        }

        //Xác thực giới tính
        if (req.getGender() != null){
            req.setGender(req.getGender());
        }

        //Xác thự ngày sinh
        if (req.getDob() != null){
            req.setDob(req.getDob());
        }

        //Lưu đối tượng đã cập nhật xuống CSDL
        try{
            userRepository.save(user);
            return ResponseEntity.ok(ApiResponse.success("Cập nhật thành công",null));
        }catch(Exception ex){
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Lỗi : " + ex));
        }
    };

    //Đăng nhập
    public ResponseEntity<ApiResponse<?>> userLogin(UserLoginDto req){
        //Tên tài khoản không được để trống
        if (req.getUsername() == null){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Tài khoản không được để trống"));
        }

        //Mật khẩu không được để trống
        if (req.getPasswords() == null){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Mật khẩu không được để trống"));
        }

        //Tìm kiếm đối tượng người dùng bằng tên tài khoản
        User user = getUserByUsername(req.getUsername());

        //TH1: Không tìm thấy đối tượng người dùng
        if (user == null){
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Tài khoản không tồn tại"));
        }
        //TH2: Tìm thấy đối tượng người dùng
        //Xác thực mật khẩu
        if (!PasswordsUtil.checkPassword(req.getPasswords(),user.getPasswords())){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Mật khẩu đăng nhập không đúng"));
        }
        //Đăng nhập thành công
        return ResponseEntity.ok(ApiResponse.success("Đăng nhập thành công, chúc một ngày học tập vui vẻ",jwtUtil.createToken(user.getId())));
    }

    //Giải mã JWT để lấy userId trong subject
    public ResponseEntity<ApiResponse<?>> decodeToken(String token){
        DecodedJWT jwt = jwtUtil.decodeToken(token);
        return ResponseEntity.ok(ApiResponse.success("Mã token",jwt.getSubject()));
    }

    //Tăng kinh nghiệm của tài khoản
    public ResponseEntity<ApiResponse<?>> increaseExp(String id, Integer exp){

        //Tìm đối tượng người dùng bằng id
        User user = getUserById(id);

        //Không tìm thấy đối tượng người dùng tương ứng
        if (user == null){
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Không tìm thấy tài khoản tương ứng"));
        }

        //Cập nhật kinh nghiệm và huy hiệu mới nếu đạt móc kinh nghiệm quy định
        Integer currentExp = user.getExp() + exp;
        Integer currentBadgeLevel = badgeService.getLevelById(user.getBadge_id());
        user.setExp(currentExp);
        boolean upgradeFlag = false;
        if (badgeService.isUpgradeBadge(currentExp,currentBadgeLevel) && currentBadgeLevel < 6){
            Integer targetBadgeId = badgeService.getBadgeIdByLevel(currentBadgeLevel + 1);
            user.setBadge_id(targetBadgeId);
            upgradeFlag = true;
        }
        //Lưu lại dữ liệu đã cập nhật
        userRepository.save(user);
        ExpUpgradeResponse response = new ExpUpgradeResponse(upgradeFlag);
        return ResponseEntity.ok(ApiResponse.success("Đã tăng " + exp + " exp",response));
    }
}
