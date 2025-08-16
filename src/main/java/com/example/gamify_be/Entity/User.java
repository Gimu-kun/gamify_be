package com.example.gamify_be.Entity;

import com.example.gamify_be.Enums.User.UserGenderEnum;
import com.example.gamify_be.Enums.User.UserRoleEnum;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User {
    @Id
    private final String id = "USER-" + UUID.randomUUID().toString().replace("-","").substring(0,10);
    private String full_name;
    private String username;
    private String passwords;
    @Enumerated(EnumType.STRING)
    private UserGenderEnum gender;
    private LocalDate dob;
    @Enumerated(EnumType.STRING)
    private UserRoleEnum role = UserRoleEnum.student;
    private Integer exp = 0;
    private Integer badge_id = 1;


    //Contructor

    public User(){}

    public User(String full_name, String username, String passwords, UserGenderEnum gender, LocalDate dob) {
        this.full_name = full_name;
        this.username = username;
        this.passwords = passwords;
        this.gender = gender;
        this.dob = dob;
    }

    //Getter and Setter


    public String getId() {
        return id;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public Integer getBadge_id() {
        return badge_id;
    }

    public void setBadge_id(Integer badge_id) {
        this.badge_id = badge_id;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", full_name='" + full_name + '\'' +
                ", username='" + username + '\'' +
                ", passwords='" + passwords + '\'' +
                ", gender=" + gender +
                ", dob=" + dob +
                ", role=" + role +
                ", exp=" + exp +
                ", badge_id=" + badge_id +
                '}';
    }
}
