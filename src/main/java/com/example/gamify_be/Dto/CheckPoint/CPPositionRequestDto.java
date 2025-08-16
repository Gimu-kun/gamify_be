package com.example.gamify_be.Dto.CheckPoint;

public class CPPositionRequestDto {
    private String roadmapId;
    private Integer section;
    private String operator;

    public CPPositionRequestDto(){}

    public CPPositionRequestDto( String roadmapId, Integer section, String operator) {
        this.roadmapId = roadmapId;
        this.section = section;
        this.operator = operator;
    }

    public String getRoadmapId() {
        return roadmapId;
    }

    public void setRoadmapId(String roadmap_id) {
        this.roadmapId = roadmap_id;
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
