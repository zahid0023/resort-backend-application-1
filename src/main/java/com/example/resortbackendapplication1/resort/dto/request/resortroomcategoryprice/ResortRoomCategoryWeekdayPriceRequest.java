package com.example.resortbackendapplication1.resort.dto.request.resortroomcategoryprice;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResortRoomCategoryWeekdayPriceRequest extends ResortRoomCategoryPriceRequest {

    /**
     * Days of week the WEEKDAY price applies to. At least one required.
     */
    @NotEmpty
    private List<Long> dayOfWeekIds;
}
