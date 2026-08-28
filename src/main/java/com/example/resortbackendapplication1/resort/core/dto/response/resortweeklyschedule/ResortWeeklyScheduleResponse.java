package com.example.resortbackendapplication1.resort.core.dto.response.resortweeklyschedule;

import com.example.resortbackendapplication1.resort.core.model.dto.ResortWeeklyScheduleDayDto;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResortWeeklyScheduleResponse {

    private final List<ResortWeeklyScheduleDayDto> weekday;
    private final List<ResortWeeklyScheduleDayDto> weekend;

    public ResortWeeklyScheduleResponse(List<ResortWeeklyScheduleDayDto> weekday, List<ResortWeeklyScheduleDayDto> weekend) {
        this.weekday = weekday;
        this.weekend = weekend;
    }
}
