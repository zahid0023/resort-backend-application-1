package com.example.resortbackendapplication1.dayofweek.dto.response.daysofweek;

import com.example.resortbackendapplication1.dayofweek.model.dto.DayOfWeekLocaleDto;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class DayOfWeekLocaleResponse {
    private final DayOfWeekLocaleDto dayOfWeekLocale;

    public DayOfWeekLocaleResponse(DayOfWeekLocaleDto dayOfWeekLocale) {
        this.dayOfWeekLocale = dayOfWeekLocale;
    }
}
