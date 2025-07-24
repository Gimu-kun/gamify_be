package com.example.acode.Utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordUtil {
    static PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public static String hashPassword(String pw){
        return passwordEncoder.encode(pw);
    };

    public static boolean checkPassword(String rawPw, String encodedPw){
        return passwordEncoder.matches(rawPw,encodedPw);
    };
}
