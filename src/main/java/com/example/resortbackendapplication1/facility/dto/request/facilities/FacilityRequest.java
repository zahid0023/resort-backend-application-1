package com.example.resortbackendapplication1.facility.dto.request.facilities;

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
public class FacilityRequest {

    @Min(0)
    private Integer sortOrder;

    @NotNull
    private IconType iconType;

    @Size(max = 2000)
    private String iconValue;

    private Map<String, Object> iconMeta;
}
