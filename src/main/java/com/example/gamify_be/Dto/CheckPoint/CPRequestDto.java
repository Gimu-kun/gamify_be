package com.example.gamify_be.Dto.CheckPoint;

public class CPRequestDto {
    private String name;
    private String description;
    private String operator;

    public CPRequestDto(){}

    public CPRequestDto(String name, String description, String operator) {
        this.name = name;
        this.description = description;
        this.operator = operator;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
