package com.example.resortbackendapplication1.resort.room.dto.request.resortroom;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UpdateResortRoomStatusRequest {

    @NotNull
    private Long roomStatusId;
}
