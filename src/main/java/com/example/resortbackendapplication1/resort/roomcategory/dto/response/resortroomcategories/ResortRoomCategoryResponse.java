package com.example.resortbackendapplication1.resort.roomcategory.dto.response.resortroomcategories;

import com.example.resortbackendapplication1.resort.roomcategory.model.dto.ResortRoomCategoryDto;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResortRoomCategoryResponse {

    private final ResortRoomCategoryDto data;

    public ResortRoomCategoryResponse(ResortRoomCategoryDto data) {
        this.data = data;
    }
}
