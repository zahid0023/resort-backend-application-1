package com.example.resortbackendapplication1.resortroomcategory.model.dto;

import com.example.resortbackendapplication1.unit.model.dto.UnitDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.math.BigDecimal;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResortRoomCategoryMetaDto {
    private Long id;
    private Integer maxAdults;
    private Integer maxChildren;
    private Integer maxInfants;
    private Integer maxOccupancy;
    private BigDecimal roomSize;
    private UnitDto roomSizeUnit;
    private Integer bedroomCount;
    private Integer bathroomCount;
    private LocalTime defaultCheckInTime;
    private LocalTime defaultCheckOutTime;
    private Boolean isExtraBedAllowed;
    private Integer maxExtraBeds;
    private Boolean isSmokingAllowed;
    private Boolean isPetsAllowed;
    private Integer minimumStayNights;
    private Integer maximumStayNights;
}
