package com.example.resortbackendapplication1.resortroomcategory.dto.response;

import com.example.resortbackendapplication1.resortroomcategory.model.dto.ResortRoomCategoryDto;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResortRoomCategoryResponse {
    private final ResortRoomCategoryDto data;

    public ResortRoomCategoryResponse(ResortRoomCategoryDto resortRoomCategory) {
        this.data = resortRoomCategory;
    }
}
