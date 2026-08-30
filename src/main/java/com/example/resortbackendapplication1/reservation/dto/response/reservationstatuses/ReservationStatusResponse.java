package com.example.resortbackendapplication1.reservation.dto.response.reservationstatuses;

import com.example.resortbackendapplication1.reservation.model.dto.ReservationStatusDto;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ReservationStatusResponse {

    private final ReservationStatusDto data;

    public ReservationStatusResponse(ReservationStatusDto data) {
        this.data = data;
    }
}
