package com.example.resortbackendapplication1.roomstatus.dto.response.roomstatuses;

import com.example.resortbackendapplication1.roomstatus.model.dto.RoomStatusDto;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RoomStatusResponse {
    private final RoomStatusDto data;

    public RoomStatusResponse(RoomStatusDto data) {
        this.data = data;
    }
}
