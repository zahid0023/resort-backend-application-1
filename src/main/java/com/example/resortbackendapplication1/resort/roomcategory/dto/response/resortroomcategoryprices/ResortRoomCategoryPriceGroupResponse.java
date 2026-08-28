package com.example.resortbackendapplication1.resort.roomcategory.dto.response.resortroomcategoryprices;

import com.example.resortbackendapplication1.resort.roomcategory.model.dto.ResortRoomCategoryPriceGroupDto;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResortRoomCategoryPriceGroupResponse {

    private final ResortRoomCategoryPriceGroupDto data;

    public ResortRoomCategoryPriceGroupResponse(ResortRoomCategoryPriceGroupDto data) {
        this.data = data;
    }
}
