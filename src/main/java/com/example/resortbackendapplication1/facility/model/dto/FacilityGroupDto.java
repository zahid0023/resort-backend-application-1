package com.example.resortbackendapplication1.facility.model.dto;

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
public class FacilityGroupDto {

    private Long id;
    private String code;
    private Integer sortOrder;
    private String iconType;
    private String iconValue;
    private Map<String, Object> iconMeta;

    private FacilityGroupLocaleDto locale;
    private List<FacilityScopeDto> facilityScopes;
}
