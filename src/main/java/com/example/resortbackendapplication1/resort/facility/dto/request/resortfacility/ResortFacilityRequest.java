package com.example.resortbackendapplication1.resort.facility.dto.request.resortfacility;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.util.Map;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResortFacilityRequest {

    /** Resort facility group this facility belongs to. Required. */
    @NotNull
    private Long resortFacilityGroupId;

    @NotNull
    private Integer sortOrder;

    @NotNull
    private Boolean isHighlighted;

    @Size(max = 100)
    private String iconType;

    private String iconValue;

    private Map<String, Object> iconMeta;
}
