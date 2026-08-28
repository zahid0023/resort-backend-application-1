package com.example.resortbackendapplication1.resort.room.dto.request.resortroomprice;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CreateResortRoomMainPriceRequest extends ResortRoomMainPriceRequest {

    /**
     * Currency all three prices below are denominated in. Must not already have an active room-level
     * override for this room.
     */
    @NotNull
    private Long currencyId;
}
