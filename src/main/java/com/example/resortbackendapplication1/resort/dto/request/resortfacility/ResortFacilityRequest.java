package com.example.resortbackendapplication1.resort.dto.request.resortfacility;

import com.example.resortbackendapplication1.commons.model.enums.IconType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.util.Map;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResortFacilityRequest {

    private Long facilityId;

    @NotNull
    @Min(value = 0, message = "sort_order must be 0 or greater")
    private Integer sortOrder;

    private Boolean isHighlighted = false;

    private IconType iconType;

    @Size(max = 2000, message = "icon_value must not exceed 2000 characters")
    private String iconValue;

    private Map<String, Object> iconMeta;
}
