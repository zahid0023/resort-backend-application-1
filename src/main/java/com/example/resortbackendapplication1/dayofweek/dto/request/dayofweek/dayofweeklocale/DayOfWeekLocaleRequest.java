package com.example.resortbackendapplication1.dayofweek.dto.request.dayofweek.dayofweeklocale;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class DayOfWeekLocaleRequest {

    @NotBlank
    @Size(max = 100)
    private String name;

    @Size(max = 20)
    private String shortName;
}
