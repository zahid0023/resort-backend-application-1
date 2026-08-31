package com.example.resortbackendapplication1.resort.roomreservation.model.dto;

import com.example.resortbackendapplication1.resort.pricing.PricingCalculator.RateType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResortRoomReservationNightlyPriceDto {

    private LocalDate date;

    private BigDecimal price;

    private RateType rateType;
}
