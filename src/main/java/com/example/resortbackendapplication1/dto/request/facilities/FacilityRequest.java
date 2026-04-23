package com.example.resortbackendapplication1.dto.request.facilities;

import com.example.resortbackendapplication1.enums.IconType;
import com.example.resortbackendapplication1.validation.IconCarrier;
import com.example.resortbackendapplication1.validation.ValidIcon;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.util.Map;

@Data
@ValidIcon
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class FacilityRequest implements IconCarrier {
    private Long facilityGroupId;

    @Size(max = 100)
    private String code;

    @Size(max = 255)
    private String name;

    private String description;

    private IconType iconType;

    @Size(max = 2000)
    private String iconValue;

    private Map<String, Object> iconMeta;

    @Min(0)
    private Integer sortOrder;
}
