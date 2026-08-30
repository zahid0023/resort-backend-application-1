package com.example.resortbackendapplication1.resort.availability.model.dto;

import com.example.resortbackendapplication1.currency.model.dto.CurrencyDto;
import com.example.resortbackendapplication1.price.model.dto.PriceUnitDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.math.BigDecimal;
import java.util.List;

/**
 * {@code nights} is one entry per night in the requested [check_in, check_out) stay, in date order — see
 * {@link com.example.resortbackendapplication1.resort.pricing.PricingCalculator} for the per-night
 * Special-over-Weekday/Weekend resolution rule {@code total} is the sum of.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AvailableRoomPriceDto {

    private CurrencyDto currency;

    private PriceUnitDto priceUnit;

    private List<NightlyRateDto> nights;

    private BigDecimal total;
}
