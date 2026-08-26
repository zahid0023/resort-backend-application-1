package com.example.resortbackendapplication1.resort.dto.request.resortroomcategoryprice;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CreateResortRoomCategoryMainPriceRequest extends ResortRoomCategoryMainPriceRequest {

    /**
     * Currency all three prices below are denominated in. Unique across the request's {@code prices} list.
     */
    @NotNull
    private Long currencyId;
}
