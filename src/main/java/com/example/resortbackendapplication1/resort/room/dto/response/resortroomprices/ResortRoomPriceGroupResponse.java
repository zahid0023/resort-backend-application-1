package com.example.resortbackendapplication1.resort.room.dto.response.resortroomprices;

import com.example.resortbackendapplication1.resort.room.model.dto.ResortRoomPriceGroupDto;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResortRoomPriceGroupResponse {

    private final ResortRoomPriceGroupDto data;

    public ResortRoomPriceGroupResponse(ResortRoomPriceGroupDto data) {
        this.data = data;
    }
}
