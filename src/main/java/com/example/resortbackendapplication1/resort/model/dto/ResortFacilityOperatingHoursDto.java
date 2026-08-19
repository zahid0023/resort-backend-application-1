package com.example.resortbackendapplication1.resort.model.dto;

import com.example.resortbackendapplication1.dayofweek.model.dto.DayOfWeekDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResortFacilityOperatingHoursDto {

    private Long id;

    private ResortFacilityDto resortFacility;

    private DayOfWeekDto dayOfWeek;

    private LocalTime opensAt;

    private LocalTime closesAt;

    private Boolean isClosed;

    private Boolean isTwentyFourHours;
}
