package com.example.resortbackendapplication1.resort.room.dto.request.resortroomprice;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.math.BigDecimal;

/** One currency's full base/weekday/weekend price override for a resort room. */
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResortRoomMainPriceRequest {

    /** Billing unit shared by base/weekday/weekend. */
    @NotNull
    private Long priceUnitId;

    /** Matches the {@code numeric(12,2)} column — at most 10 integer digits and 2 fraction digits. */
    @NotNull
    @DecimalMin(value = "0")
    @Digits(integer = 10, fraction = 2)
    private BigDecimal basePrice;

    /** Cannot exceed {@link #basePrice}. */
    @NotNull
    @DecimalMin(value = "0")
    @Digits(integer = 10, fraction = 2)
    private BigDecimal weekdayPrice;

    /** Cannot exceed {@link #basePrice}. */
    @NotNull
    @DecimalMin(value = "0")
    @Digits(integer = 10, fraction = 2)
    private BigDecimal weekendPrice;
}
