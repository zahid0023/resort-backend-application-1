package com.example.resortbackendapplication1.resort.reservation.dto.request.reservation;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UpdateReservationStatusRequest {

    @NotNull
    private Long reservationStatusId;
}
