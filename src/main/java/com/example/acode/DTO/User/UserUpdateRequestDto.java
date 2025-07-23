package com.example.acode.DTO.User;

import com.example.acode.Enum.UserRoleEnum;
import jakarta.persistence.Column;

public class UserUpdateRequestDto {
    private String fullname;
    private String passwords;
    @Column(nullable = false)
    private String confirmPasswords;
    private UserRoleEnum role;

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPasswords() {
        return passwords;
    }

    public void setPasswords(String passwords) {
        this.passwords = passwords;
    }

    public UserRoleEnum getRole() {
        return role;
    }

    public void setRole(UserRoleEnum role) {
        this.role = role;
    }

    public String getConfirmPasswords() {
        return confirmPasswords;
    }

    public void setConfirmPasswords(String confirmPasswords) {
        this.confirmPasswords = confirmPasswords;
    }
}
