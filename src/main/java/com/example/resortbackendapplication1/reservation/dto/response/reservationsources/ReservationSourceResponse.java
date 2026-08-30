package com.example.resortbackendapplication1.reservation.dto.response.reservationsources;

import com.example.resortbackendapplication1.reservation.model.dto.ReservationSourceDto;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ReservationSourceResponse {

    private final ReservationSourceDto data;

    public ReservationSourceResponse(ReservationSourceDto data) {
        this.data = data;
    }
}
