package com.example.acode.Service;

import com.example.acode.DTO.User.UserCreationRequestDto;
import com.example.acode.DTO.User.UserUpdateRequestDto;
import com.example.acode.Entity.User;
import com.example.acode.Enum.UserAchievementEnum;
import com.example.acode.Repository.UserRepository;
import com.example.acode.Utils.PasswordUtil;
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

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public Optional<User> getUserById(String id) {
        return userRepository.findById(id);
    }

    private boolean isUsernameExisted(String username){
        Optional<User> user = userRepository.findByUsername(username);
        return user.isPresent();
    }

    public ResponseEntity<String> createUser(UserCreationRequestDto request){
        //Validate
        if (isUsernameExisted(request.getUsername())){
            return new ResponseEntity<>("Tài khoản đã tồn tại",HttpStatus.CONFLICT);
        }

        if (request.getUsername().length() < 8){
            return new ResponseEntity<>("Tài khoản phải có ít nhất 8 ký tự",HttpStatus.BAD_REQUEST);
        }

        if (request.getUsername().isBlank()){
            return new ResponseEntity<>("Tài khoản không được để trống",HttpStatus.BAD_REQUEST);
        }

        if (request.getFullname().isBlank()){
            return new ResponseEntity<>("Họ tên không được để trống",HttpStatus.BAD_REQUEST);
        }

        if (request.getPasswords().isBlank()){
            return new ResponseEntity<>("Mật khẩu không được để trống",HttpStatus.BAD_REQUEST);
        }

        if (request.getPasswords().length() < 8){
            return new ResponseEntity<>("Mật khẩu phải có ít nhất 8 ký tự",HttpStatus.BAD_REQUEST);
        }

        if (request.getDob() == null){
            return new ResponseEntity<>("Ngày sinh không được để trống",HttpStatus.BAD_REQUEST);
        }

        if (request.getRole() == null){
            return new ResponseEntity<>("Role không được để trống",HttpStatus.BAD_REQUEST);
        }

        User nUser = new User(
                request.getUsername(),
                request.getFullname(),
                PasswordUtil.hashPassword(request.getPasswords()),
                request.getGender(),
                request.getDob(),
                request.getRole()
        );
        System.out.println(nUser.toString());
        userRepository.save(nUser);
        return ResponseEntity.ok("Tạo tài khoản thành công ");
    }

    public ResponseEntity<String> updateUser(String id, UserUpdateRequestDto request){
        //Tìm đối tượng user
        Optional<User> optUser = userRepository.findById(id);
        if (optUser.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        //Xác thực mật khẩu
        if (request.getConfirmPasswords() == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cần mật khẩu để cập nhật thông tin");
        }

        User user = optUser.get();
        if (!PasswordUtil.checkPassword(request.getConfirmPasswords(),user.getPasswords())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Mật khẩu xác thực không đúng");
        }

        //Cập nhật thông tin tương ứng
        if(request.getFullname() != null){
            user.setFullname(request.getFullname());
        }

        if(request.getRole() != null){
            user.setRole(request.getRole());
        }

        if (request.getPasswords() != null){
            user.setPasswords(PasswordUtil.hashPassword(request.getPasswords()));
        }

        userRepository.save(user);
        return ResponseEntity.ok("Cập nhật thông tin thành công");
    }

    public ResponseEntity<String> increaseExp(String id, Integer exp){
        //Tìm đối tượng user
        Optional<User> optUser = userRepository.findById(id);
        if (optUser.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        User user = optUser.get();
        user.setExp(user.getExp() + exp);

        if (user.getExp() > 10000){
            user.setAchievement(UserAchievementEnum.king);
        }else if (user.getExp() > 7000){
            user.setAchievement(UserAchievementEnum.royal);
        }else if (user.getExp() > 5000){
            user.setAchievement(UserAchievementEnum.knight);
        }else if (user.getExp() > 2000){
            user.setAchievement(UserAchievementEnum.hunter);
        }

        userRepository.save(user);
        return ResponseEntity.ok("Tăng kinh nghiệm thành công!");
    }
}
