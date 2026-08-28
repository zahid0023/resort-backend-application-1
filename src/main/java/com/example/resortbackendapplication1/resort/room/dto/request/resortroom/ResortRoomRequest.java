package com.example.resortbackendapplication1.resort.room.dto.request.resortroom;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResortRoomRequest {

    @NotNull
    private Integer sortOrder;

    private Integer floorNumber;

    @Size(max = 100)
    private String building;
}
