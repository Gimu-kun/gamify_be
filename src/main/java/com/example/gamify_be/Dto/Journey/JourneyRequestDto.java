package com.example.gamify_be.Dto.Journey;

public class JourneyRequestDto {
    private String title;
    private String description;
    private String operator;

    public JourneyRequestDto(){}

    public JourneyRequestDto(String title, String description, String operator) {
        this.title = title;
        this.description = description;
        this.operator = operator;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }
}
