package com.example.resortbackendapplication1.resort.dto.request.resortfacilityoperatinghours;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.time.LocalTime;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResortFacilityOperatingHoursWindowRequest {

    @NotNull
    private LocalTime opensAt;

    @NotNull
    private LocalTime closesAt;
}
