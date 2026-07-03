package com.example.resortbackendapplication1.roomcategory.dto.response.roomcategories;

import com.example.resortbackendapplication1.roomcategory.model.dto.RoomCategoryDto;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RoomCategoryResponse {

    private final RoomCategoryDto data;

    public RoomCategoryResponse(RoomCategoryDto roomCategory) {
        this.data = roomCategory;
    }
}
