package com.example.resortbackendapplication1.resort.facility.dto.request.resortfacilityoperatinghours;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResortFacilityOperatingHoursDayScheduleRequest {

    @NotNull
    private Long dayOfWeekId;

    @NotNull
    private Boolean isClosed;

    @NotNull
    private Boolean isTwentyFourHours;

    @Valid
    private List<@Valid ResortFacilityOperatingHoursWindowRequest> windows = new ArrayList<>();
}
