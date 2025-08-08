package com.example.gamify_be.Dto.User;

import com.example.gamify_be.Enums.User.UserGenderEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.time.LocalDate;

public class UserUpdateRequestDto {
    private String full_name;
    private String passwords;
    private String passwords_confirm;
    @Enumerated(EnumType.STRING)
    private UserGenderEnum gender;
    private LocalDate dob;

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

    public String getPasswords_confirm() {
        return passwords_confirm;
    }

    public void setPasswords_confirm(String passwords_confirm) {
        this.passwords_confirm = passwords_confirm;
    }

    @Override
    public String toString() {
        return "UserUpdateRequestDto{" +
                "full_name='" + full_name + '\'' +
                ", passwords='" + passwords + '\'' +
                ", passwords_confirm='" + passwords_confirm + '\'' +
                ", gender=" + gender +
                ", dob=" + dob +
                '}';
    }
}
