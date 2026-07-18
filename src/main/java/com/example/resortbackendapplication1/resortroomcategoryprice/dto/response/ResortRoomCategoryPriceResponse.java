package com.example.resortbackendapplication1.resortroomcategoryprice.dto.response;

import com.example.resortbackendapplication1.resortroomcategoryprice.model.dto.ResortRoomCategoryPriceDto;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResortRoomCategoryPriceResponse {
    private final ResortRoomCategoryPriceDto data;

    public ResortRoomCategoryPriceResponse(ResortRoomCategoryPriceDto resortRoomCategoryPrice) {
        this.data = resortRoomCategoryPrice;
    }
}
