package com.example.resortbackendapplication1.dto.request.resortfacilitygroups;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.util.Map;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResortFacilityGroupRequest {

    @Size(max = 255)
    private String name;

    private String description;

    @Min(0)
    private Integer sortOrder;

    @Size(max = 100)
    private String iconType;

    @Size(max = 2000)
    private String iconValue;

    private Map<String, Object> iconMeta;
}
