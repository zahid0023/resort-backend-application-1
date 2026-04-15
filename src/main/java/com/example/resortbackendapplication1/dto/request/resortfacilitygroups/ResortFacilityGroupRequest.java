package com.example.resortbackendapplication1.dto.request.resortfacilitygroups;

import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResortFacilityGroupRequest {
    private String name;
    private String description;
    private Integer sortOrder;
}
