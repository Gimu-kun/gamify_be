package com.example.acode.Entity;

import com.example.acode.Enum.UserAchievementEnum;
import com.example.acode.Enum.UserGenderEnum;
import com.example.acode.Enum.UserRoleEnum;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import org.antlr.v4.runtime.misc.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class User {
    @Id
    @Column(name = "id", nullable = false)
    private final String id = UUID.randomUUID().toString();
    private String username;
    private String fullname;
    private String passwords;

    @Enumerated(EnumType.STRING)
    private UserGenderEnum gender;
    private LocalDate dob;

    @Enumerated(EnumType.STRING)
    private UserRoleEnum role;
    private Integer exp = 0;

    @Enumerated(EnumType.STRING)
    private UserAchievementEnum achievement = UserAchievementEnum.valueOf("newbie");
    private LocalDateTime created_at = LocalDateTime.now();
    private LocalDateTime updated_at = LocalDateTime.now();

    public User(){};

    public User(String username, String fullname, String passwords, UserGenderEnum gender, LocalDate dob, UserRoleEnum role) {
        this.username = username;
        this.fullname = fullname;
        this.passwords = passwords;
        this.gender = gender;
        this.dob = dob;
        this.role = role;
    }

    public String getId() {
        return id;
    }

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

    public Integer getExp() {
        return exp;
    }

    public void setExp(Integer exp) {
        this.exp = exp;
    }

    public UserAchievementEnum getAchievement() {
        return achievement;
    }

    public void setAchievement(UserAchievementEnum achievement) {
        this.achievement = achievement;
    }

    public LocalDateTime getCreate_at() {
        return created_at;
    }

    public void setCreate_at(LocalDateTime create_at) {
        this.created_at = create_at;
    }

    public LocalDateTime getUpdate_at() {
        return updated_at;
    }

    public void setUpdate_at(LocalDateTime update_at) {
        this.updated_at = update_at;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", fullname='" + fullname + '\'' +
                ", passwords='" + passwords + '\'' +
                ", gender=" + gender +
                ", dob=" + dob +
                ", role=" + role +
                ", exp=" + exp +
                ", achievement='" + achievement + '\'' +
                ", created_at=" + created_at +
                ", updated_at=" + updated_at +
                '}';
    }
}
