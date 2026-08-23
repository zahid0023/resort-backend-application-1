package com.example.resortbackendapplication1.resort.dto.request.resortroomcategorybed;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResortRoomCategoryBedRequest {

    @NotNull
    @Min(1)
    private Integer quantity;

    @NotNull
    private Boolean isExtraBedAllowed;

    @NotNull
    @Min(0)
    private Integer maxExtraBeds;
}
