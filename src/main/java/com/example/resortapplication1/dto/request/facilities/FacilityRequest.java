package com.example.resortapplication1.dto.request.facilities;

import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class FacilityRequest {
    private Long facilityGroupId;
    private String code;
    private String name;
    private String description;
    private String type;
    private String icon;
}
