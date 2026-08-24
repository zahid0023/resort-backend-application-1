package com.example.resortbackendapplication1.resort.dto.request.resortroomcategoryprice;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CreateResortRoomCategoryPriceRequest extends ResortRoomCategoryPriceRequest {

    /**
     * BASE/WEEKDAY/WEEKEND/HOLIDAY/SPECIAL. Immutable after creation.
     */
    @NotNull
    private Long priceTypeId;

    /**
     * Billing unit (PER_NIGHT/PER_DAY/PER_PERSON/...). Immutable after creation.
     */
    @NotNull
    private Long priceUnitId;

    /**
     * Currency of {@code price}. Immutable after creation.
     */
    @NotNull
    private Long currencyId;

}
