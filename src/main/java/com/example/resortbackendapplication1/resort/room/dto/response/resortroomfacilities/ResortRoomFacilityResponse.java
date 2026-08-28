package com.example.resortbackendapplication1.resort.room.dto.response.resortroomfacilities;

import com.example.resortbackendapplication1.resort.room.model.dto.ResortRoomFacilityDto;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResortRoomFacilityResponse {

    private final ResortRoomFacilityDto data;

    public ResortRoomFacilityResponse(ResortRoomFacilityDto data) {
        this.data = data;
    }
}
