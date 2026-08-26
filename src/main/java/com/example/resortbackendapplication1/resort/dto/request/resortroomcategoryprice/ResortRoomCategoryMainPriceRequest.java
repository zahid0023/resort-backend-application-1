package com.example.resortbackendapplication1.resort.dto.request.resortroomcategoryprice;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

/**
 * One currency's full BASE/WEEKDAY/WEEKEND price set, submitted as part of
 * {@link com.example.resortbackendapplication1.resort.dto.request.resortroomcategory.CreateResortRoomCategoryRequest}.
 * HOLIDAY/SPECIAL prices are not created here — add them afterward via
 * {@code POST .../room-categories/{room-category-id}/prices}.
 */
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResortRoomCategoryMainPriceRequest {

    @Valid
    @NotNull
    private ResortRoomCategoryBasePriceRequest basePriceRequest;

    @Valid
    @NotNull
    private ResortRoomCategoryWeekdayPriceRequest weekdayPrice;

    @Valid
    @NotNull
    private ResortRoomCategoryWeekendPriceRequest weekendPrice;
}
