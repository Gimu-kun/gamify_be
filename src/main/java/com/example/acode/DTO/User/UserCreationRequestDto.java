package com.example.acode.DTO.User;
import com.example.acode.Enum.UserGenderEnum;
import com.example.acode.Enum.UserRoleEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.time.LocalDate;

public class UserCreationRequestDto {
    private String username;
    private String fullname;
    private String passwords;
    @Enumerated(EnumType.STRING)
    private UserGenderEnum gender;
    private LocalDate dob;
    @Enumerated(EnumType.STRING)
    private UserRoleEnum role;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

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

    public UserGenderEnum getGender() {
        return gender;
    }

    public void setGender(UserGenderEnum gender) {
        this.gender = gender;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public UserRoleEnum getRole() {
        return role;
    }

    public void setRole(UserRoleEnum role) {
        this.role = role;
    }

}
