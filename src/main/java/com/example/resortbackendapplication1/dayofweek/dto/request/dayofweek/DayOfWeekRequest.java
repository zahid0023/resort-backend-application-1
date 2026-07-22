package com.example.resortbackendapplication1.dayofweek.dto.request.dayofweek;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class DayOfWeekRequest {

    @NotNull
    private Integer displayOrder;
}
