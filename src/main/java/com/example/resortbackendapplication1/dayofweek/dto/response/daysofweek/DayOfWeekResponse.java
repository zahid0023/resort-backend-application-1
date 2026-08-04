package com.example.resortbackendapplication1.dayofweek.dto.response.daysofweek;

import com.example.resortbackendapplication1.dayofweek.model.dto.DayOfWeekDto;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class DayOfWeekResponse {
    private final DayOfWeekDto data;

    public DayOfWeekResponse(DayOfWeekDto data) {
        this.data = data;
    }
}
