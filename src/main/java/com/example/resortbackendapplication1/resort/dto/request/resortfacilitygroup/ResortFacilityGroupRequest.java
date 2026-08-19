package com.example.resortbackendapplication1.resort.dto.request.resortfacilitygroup;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.util.Map;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResortFacilityGroupRequest {

    @NotNull
    private Integer sortOrder;

    @Size(max = 100)
    private String iconType;

    private String iconValue;

    private Map<String, Object> iconMeta;
}
