package com.example.resortbackendapplication1.dto.response.rooms;

import com.example.resortbackendapplication1.model.dto.RoomDto;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RoomResponse {
    private RoomDto data;

    public RoomResponse(RoomDto room) {
        this.data = room;
    }
}
