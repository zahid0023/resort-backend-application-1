package com.example.resortbackendapplication1.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.util.Map;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResortFacilityDto {
    private Long id;
    private Long resortFacilityGroupId;
    private Long facilityId;
    private String name;
    private String description;
    private Integer sortOrder;
    private String iconType;
    private String iconValue;
    private Map<String, Object> iconMeta;
}
