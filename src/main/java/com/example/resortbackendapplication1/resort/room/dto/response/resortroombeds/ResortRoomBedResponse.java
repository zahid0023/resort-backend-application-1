package com.example.resortbackendapplication1.resort.room.dto.response.resortroombeds;

import com.example.resortbackendapplication1.resort.room.model.dto.ResortRoomBedDto;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResortRoomBedResponse {

    private final ResortRoomBedDto data;

    public ResortRoomBedResponse(ResortRoomBedDto data) {
        this.data = data;
    }
}
