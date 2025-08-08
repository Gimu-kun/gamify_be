package com.example.gamify_be.Dto.User;

public class UserLoginDto {
    private String username;
    private String passwords;

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

    @Override
    public String toString() {
        return "UserLoginDto{" +
                "username='" + username + '\'' +
                ", passwords='" + passwords + '\'' +
                '}';
    }
}
