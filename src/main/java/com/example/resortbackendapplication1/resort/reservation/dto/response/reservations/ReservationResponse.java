package com.example.resortbackendapplication1.resort.reservation.dto.response.reservations;

import com.example.resortbackendapplication1.resort.reservation.model.dto.ReservationDto;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ReservationResponse {

    private final ReservationDto data;

    public ReservationResponse(ReservationDto data) {
        this.data = data;
    }
}
