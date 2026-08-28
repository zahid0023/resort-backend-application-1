package com.example.resortbackendapplication1.resort.roomcategory.dto.request.resortroomcategoryprice;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CreateResortRoomCategorySpecialPriceRequest extends ResortRoomCategorySpecialPriceRequest {

    @NotNull
    private Long currencyId;
}
