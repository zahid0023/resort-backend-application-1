package com.example.resortbackendapplication1.resort.dto.request.resortweeklyschedule;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.util.List;

/**
 * Replaces the resort's entire weekly schedule atomically — shared by every room category at the resort, not
 * scoped per currency. Used both by the initial write on {@code POST /resorts} (as the mandatory
 * {@code weekly_schedule} field) and by {@code PUT /resorts/{resort-id}/weekly-schedule} afterward, since both
 * are the same whole-set-replace shape. See {@link com.example.resortbackendapplication1.resort.validation.ResortWeeklyScheduleValidator}
 * for the no-duplicate/no-overlap rules applied on top of the {@code @NotEmpty} below.
 */
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResortWeeklyScheduleRequest {

    @NotEmpty
    private List<Long> weekdayDayOfWeekIds;

    @NotEmpty
    private List<Long> weekendDayOfWeekIds;
}
