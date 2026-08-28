package com.example.resortbackendapplication1.resort.facility.dto.response.resortfacilityoperatinghours;

import com.example.resortbackendapplication1.resort.facility.model.dto.ResortFacilityOperatingHoursDto;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResortFacilityOperatingHoursScheduleResponse {

    private final List<ResortFacilityOperatingHoursDto> data;

    public ResortFacilityOperatingHoursScheduleResponse(List<ResortFacilityOperatingHoursDto> data) {
        this.data = data;
    }
}
