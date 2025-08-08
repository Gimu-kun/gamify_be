package com.example.gamify_be.Dto.CheckIn;

import jakarta.persistence.Column;

import java.time.LocalDate;
import java.time.LocalTime;

public class CheckInRequestDto {
    private String userId;
    @Column(name = "checkin_date")
    private LocalDate checkInDate;
    @Column(name = "checkin_time")
    private LocalTime checkInTime;

    public CheckInRequestDto(){}

    public CheckInRequestDto(String userId, LocalDate checkInDate, LocalTime checkInTime) {
        this.userId = userId;
        this.checkInDate = checkInDate;
        this.checkInTime = checkInTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(LocalDate checkInDate) {
        this.checkInDate = checkInDate;
    }

    public LocalTime getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(LocalTime checkInTime) {
        this.checkInTime = checkInTime;
    }
}
