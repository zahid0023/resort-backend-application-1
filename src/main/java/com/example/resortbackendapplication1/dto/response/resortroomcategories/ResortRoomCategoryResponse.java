package com.example.resortbackendapplication1.dto.response.resortroomcategories;

import com.example.resortbackendapplication1.model.dto.ResortRoomCategoryDto;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResortRoomCategoryResponse {
    private ResortRoomCategoryDto data;

    public ResortRoomCategoryResponse(ResortRoomCategoryDto resortRoomCategory) {
        this.data = resortRoomCategory;
    }
}
