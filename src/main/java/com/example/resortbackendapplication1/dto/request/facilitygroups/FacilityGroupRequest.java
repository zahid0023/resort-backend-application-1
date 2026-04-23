package com.example.resortbackendapplication1.dto.request.facilitygroups;

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
public class FacilityGroupRequest implements IconCarrier {

    @Size(max = 100, message = "code must not exceed 100 characters")
    private String code;

    @Size(max = 255, message = "name must not exceed 255 characters")
    private String name;

    private String description;

    @Min(value = 0, message = "sort_order must be 0 or greater")
    private Integer sortOrder;

    private IconType iconType;

    @Size(max = 2000, message = "icon_value must not exceed 2000 characters")
    private String iconValue;

    private Map<String, Object> iconMeta;
}
