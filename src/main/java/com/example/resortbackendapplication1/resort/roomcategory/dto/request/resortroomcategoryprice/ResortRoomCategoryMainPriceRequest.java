package com.example.resortbackendapplication1.resort.roomcategory.dto.request.resortroomcategoryprice;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.math.BigDecimal;

/**
 * One currency's full base/weekday/weekend price set, submitted as part of
 * {@link com.example.resortbackendapplication1.resort.dto.request.resortroomcategory.CreateResortRoomCategoryRequest}.
 * Special prices are not created here — add them afterward via
 * {@code POST .../room-categories/{resort-room-category-id}/prices}.
 */
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResortRoomCategoryMainPriceRequest {

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
