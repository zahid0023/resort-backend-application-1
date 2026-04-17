package com.example.resortbackendapplication1.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RoomPricePeriodDto {
    private Long id;
    private Long roomId;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal price;
    private Integer priority;
    private Long priceTypeId;
    private String priceTypeCode;
    private String priceTypeName;
}
