package com.example.resortbackendapplication1.roomstatus.dto.request.roomstatus;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RoomStatusRequest {

    @NotNull
    private Integer sortOrder;

}
