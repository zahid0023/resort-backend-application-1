package com.example.resortbackendapplication1.resort.dto.request.resortfacilityoperatinghours;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.util.List;

/**
 * One entry per day of week is required — a facility's schedule is set for the whole week atomically, never
 * for a subset of days. See {@link ResortFacilityOperatingHoursDayScheduleRequest} for the per-day shape.
 */
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SetResortFacilityOperatingHoursScheduleRequest {

    @NotEmpty
    @Valid
    private List<@Valid ResortFacilityOperatingHoursDayScheduleRequest> days;
}
