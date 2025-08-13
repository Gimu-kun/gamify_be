package com.example.gamify_be.Dto.ApiResponse;

import org.springframework.http.HttpStatus;

public class ApiResponse<T> {
    private String message;
    private T data;

    public ApiResponse() {}

    public ApiResponse( String message, T data) {
        this.message = message;
        this.data = data;
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(message, data);
    }

    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(message, null);
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

}
