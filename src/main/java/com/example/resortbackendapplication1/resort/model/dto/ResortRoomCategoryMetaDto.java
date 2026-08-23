package com.example.resortbackendapplication1.resort.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.math.BigDecimal;

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

    private Long roomSizeUnitId;

    private Integer bedroomCount;

    private Integer bathroomCount;

    private Integer minimumStayNights;

    private Integer maximumStayNights;
}
