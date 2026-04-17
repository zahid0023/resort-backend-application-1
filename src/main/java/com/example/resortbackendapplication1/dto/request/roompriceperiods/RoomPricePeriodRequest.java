package com.example.resortbackendapplication1.dto.request.roompriceperiods;

import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RoomPricePeriodRequest {
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal price;
    private Integer priority;
    private Long priceTypeId;
}
