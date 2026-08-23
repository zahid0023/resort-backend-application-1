package com.example.resortbackendapplication1.resort.dto.request.resortroomcategorymeta;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.math.BigDecimal;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResortRoomCategoryMetaRequest {

    @NotNull
    private Integer maxAdults;

    @NotNull
    private Integer maxChildren;

    @NotNull
    private Integer maxInfants;

    @NotNull
    private Integer maxOccupancy;

    private BigDecimal roomSize;

    private Long roomSizeUnitId;

    @NotNull
    private Integer bedroomCount;

    @NotNull
    private Integer bathroomCount;

    @NotNull
    private Integer minimumStayNights;

    private Integer maximumStayNights;
}
