package com.example.gamify_be.Dto.CheckPoint;

public class CPPositionRequestDto {
    private String journeyId;
    private Integer section;
    private String operator;

    public CPPositionRequestDto(){}

    public CPPositionRequestDto( String journeyId, Integer section, String operator) {
        this.journeyId = journeyId;
        this.section = section;
        this.operator = operator;
    }

    public String getJourneyId() {
        return journeyId;
    }

    public void setJourneyId(String journey_id) {
        this.journeyId = journey_id;
    }

    public Integer getSection() {
        return section;
    }

    public void setSection(Integer section) {
        this.section = section;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }
}
