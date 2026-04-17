package com.example.resortbackendapplication1.dto.response.roompriceperiods;

import com.example.resortbackendapplication1.model.dto.RoomPricePeriodDto;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RoomPricePeriodResponse {
    private RoomPricePeriodDto data;

    public RoomPricePeriodResponse(RoomPricePeriodDto roomPricePeriod) {
        this.data = roomPricePeriod;
    }
}
