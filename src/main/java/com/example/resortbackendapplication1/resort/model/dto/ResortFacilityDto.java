package com.example.resortbackendapplication1.resort.model.dto;

import com.example.resortbackendapplication1.commons.model.enums.IconType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResortFacilityDto {
    private Long id;
    private Long resortId;
    private Long resortFacilityGroupId;
    private Long facilityId;
    private Integer sortOrder;
    private IconType iconType;
    private String iconValue;
    private Map<String, Object> iconMeta;
    private List<ResortFacilityLocaleDto> locales;
}
