package com.example.resortbackendapplication1.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.math.BigDecimal;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RoomDto {
    private Long id;
    private Long resortRoomCategoryId;
    private String name;
    private String description;
    private String roomNumber;
    private Integer floor;
    private Integer maxAdults;
    private Integer maxChildren;
    private Integer maxOccupancy;
    private BigDecimal basePrice;
}
