package com.example.resortbackendapplication1.reservation.dto.request.reservationstatus;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ReservationStatusRequest {

    @NotNull
    private Integer sortOrder;
}
