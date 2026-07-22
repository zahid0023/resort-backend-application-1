package com.example.resortbackendapplication1.dayofweek.dto.request.dayofweek.dayofweeklocale;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CreateDayOfWeekLocaleRequest extends DayOfWeekLocaleRequest {

    @NotNull
    private Long localeId;
}
