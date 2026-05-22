package com.example.resortbackendapplication1.dto.response.roomcategories;

import com.example.resortbackendapplication1.model.dto.RoomCategoryDto;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RoomCategoryResponse {
    private final RoomCategoryDto roomCategory;

    public RoomCategoryResponse(RoomCategoryDto roomCategory) {
        this.roomCategory = roomCategory;
    }
}
