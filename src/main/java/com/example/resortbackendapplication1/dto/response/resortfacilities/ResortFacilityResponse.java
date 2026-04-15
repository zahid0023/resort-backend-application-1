package com.example.resortbackendapplication1.dto.response.resortfacilities;

import com.example.resortbackendapplication1.model.dto.ResortFacilityDto;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResortFacilityResponse {
    private ResortFacilityDto data;

    public ResortFacilityResponse(ResortFacilityDto resortFacility) {
        this.data = resortFacility;
    }
}
