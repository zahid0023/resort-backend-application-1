package com.example.resortbackendapplication1.reservation.dto.request.reservationsource;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ReservationSourceRequest {

    @NotNull
    private Integer sortOrder;
}
