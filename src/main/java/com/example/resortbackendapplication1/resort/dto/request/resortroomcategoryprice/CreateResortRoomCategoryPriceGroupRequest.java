package com.example.resortbackendapplication1.resort.dto.request.resortroomcategoryprice;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.math.BigDecimal;
import java.util.List;

/**
 * One currency's full BASE/WEEKDAY/WEEKEND price set, submitted as part of
 * {@link com.example.resortbackendapplication1.resort.dto.request.resortroomcategory.CreateResortRoomCategoryRequest}.
 * HOLIDAY/SPECIAL prices are not created here — add them afterward via
 * {@code POST .../room-categories/{room-category-id}/prices}.
 */
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CreateResortRoomCategoryPriceGroupRequest {

    /**
     * Currency all three prices below are denominated in. Unique across the request's {@code prices} list.
     */
    @NotNull
    private Long currencyId;

    @NotNull
    private Long basePriceUnitId;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal basePrice;

    @NotNull
    private Long weekdayPriceUnitId;

    /**
     * Cannot exceed {@link #basePrice} for the same currency.
     */
    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal weekdayPrice;

    /**
     * Days of week the WEEKDAY price applies to. At least one required.
     */
    @NotEmpty
    private List<Long> weekdayDayOfWeekIds;

    @NotNull
    private Long weekendPriceUnitId;

    /**
     * Cannot exceed {@link #basePrice} for the same currency.
     */
    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal weekendPrice;

    /**
     * Days of week the WEEKEND price applies to. At least one required.
     */
    @NotEmpty
    private List<Long> weekendDayOfWeekIds;
}
