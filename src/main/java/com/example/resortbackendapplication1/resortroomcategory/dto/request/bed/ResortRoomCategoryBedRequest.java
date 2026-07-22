package com.example.resortbackendapplication1.resortroomcategory.dto.request.bed;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResortRoomCategoryBedRequest {

    @NotNull
    private Long bedTypeId;

    @NotNull
    @Min(value = 1, message = "quantity must be at least 1")
    private Integer quantity;
}
