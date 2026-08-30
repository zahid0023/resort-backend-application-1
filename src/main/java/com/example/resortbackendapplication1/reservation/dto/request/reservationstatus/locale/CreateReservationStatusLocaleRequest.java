package com.example.resortbackendapplication1.reservation.dto.request.reservationstatus.locale;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CreateReservationStatusLocaleRequest extends ReservationStatusLocaleRequest {

    @NotNull
    private Long localeId;
}
