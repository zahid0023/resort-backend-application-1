package com.example.resortbackendapplication1.dto.request.rooms;

import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.math.BigDecimal;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RoomRequest {
    private String name;
    private String description;
    private String roomNumber;
    private Integer floor;
    private Integer maxAdults;
    private Integer maxChildren;
    private BigDecimal basePrice;
}
