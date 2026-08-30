package com.example.resortbackendapplication1.resort.room.dto.response.resortroommeta;

import com.example.resortbackendapplication1.resort.room.model.dto.ResortRoomMetaDto;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResortRoomMetaResponse {

    private final ResortRoomMetaDto data;

    public ResortRoomMetaResponse(ResortRoomMetaDto data) {
        this.data = data;
    }
}
