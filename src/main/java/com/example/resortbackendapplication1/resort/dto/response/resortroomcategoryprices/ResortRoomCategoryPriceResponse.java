package com.example.resortbackendapplication1.resort.dto.response.resortroomcategoryprices;

import com.example.resortbackendapplication1.resort.model.dto.ResortRoomCategoryPriceDto;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResortRoomCategoryPriceResponse {

    private final ResortRoomCategoryPriceDto data;

    public ResortRoomCategoryPriceResponse(ResortRoomCategoryPriceDto data) {
        this.data = data;
    }
}
