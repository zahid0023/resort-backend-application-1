package com.example.resortbackendapplication1.resort.model.dto;

import com.example.resortbackendapplication1.commons.model.enums.IconType;
import com.example.resortbackendapplication1.facility.model.dto.FacilityDto;
import com.example.resortbackendapplication1.facilitypricetype.model.dto.FacilityPriceTypeDto;
import com.example.resortbackendapplication1.resortfacilityprice.model.dto.ResortFacilityPriceDto;
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
    private ResortFacilityGroupDto resortFacilityGroup;
    private FacilityDto platformFacility;
    private FacilityPriceTypeDto facilityPriceType;
    private Integer sortOrder;
    private Boolean isHighlighted;
    private IconType iconType;
    private String iconValue;
    private Map<String, Object> iconMeta;
    private List<ResortFacilityLocaleDto> locales;
    private List<ResortFacilityPriceDto> prices;
}
