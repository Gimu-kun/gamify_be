package com.example.gamify_be.Dto.User;

import com.example.gamify_be.Enums.User.UserGenderEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.time.LocalDate;

public class UserCreationRequestDto {
    private String username;
    private String full_name;
    private String passwords;
    private UserGenderEnum gender;
    private LocalDate dob;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
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

    @Override
    public String toString() {
        return "UserCreationRequestDto{" +
                "username='" + username + '\'' +
                ", full_name='" + full_name + '\'' +
                ", passwords='" + passwords + '\'' +
                ", gender=" + gender +
                ", dob=" + dob +
                '}';
    }
}
