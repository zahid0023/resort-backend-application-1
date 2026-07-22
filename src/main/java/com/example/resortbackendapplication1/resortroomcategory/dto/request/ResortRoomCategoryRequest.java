package com.example.resortbackendapplication1.resortroomcategory.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResortRoomCategoryRequest {

    @NotNull
    @Min(value = 0, message = "sort_order must be 0 or greater")
    private Integer sortOrder;
}
