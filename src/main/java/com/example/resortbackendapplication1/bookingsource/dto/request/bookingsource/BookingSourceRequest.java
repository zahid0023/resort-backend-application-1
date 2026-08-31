package com.example.resortbackendapplication1.bookingsource.dto.request.bookingsource;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BookingSourceRequest {

    @NotNull
    private Integer sortOrder;
}
