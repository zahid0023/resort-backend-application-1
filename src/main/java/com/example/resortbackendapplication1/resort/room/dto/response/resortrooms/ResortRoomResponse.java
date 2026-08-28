package com.example.resortbackendapplication1.resort.room.dto.response.resortrooms;

import com.example.resortbackendapplication1.resort.room.model.dto.ResortRoomDto;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResortRoomResponse {

    private final ResortRoomDto data;

    public ResortRoomResponse(ResortRoomDto data) {
        this.data = data;
    }
}
