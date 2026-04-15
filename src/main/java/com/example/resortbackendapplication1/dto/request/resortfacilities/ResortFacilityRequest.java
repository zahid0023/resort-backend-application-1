package com.example.resortbackendapplication1.dto.request.resortfacilities;

import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResortFacilityRequest {
    private String name;
    private String description;
    private String icon;
    private String value;
}
