package com.example.resortbackendapplication1.dto.response.roomcategories;

import com.example.resortbackendapplication1.model.dto.RoomCategoryDto;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RoomCategoryResponse {
    private RoomCategoryDto data;

    public RoomCategoryResponse(RoomCategoryDto roomCategory) {
        this.data = roomCategory;
    }
}
