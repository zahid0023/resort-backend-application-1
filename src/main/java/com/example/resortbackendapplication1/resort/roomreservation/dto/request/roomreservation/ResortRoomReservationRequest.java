package com.example.resortbackendapplication1.resort.roomreservation.dto.request.roomreservation;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResortRoomReservationRequest {

    @NotNull
    private Long reservationStatusId;
}
