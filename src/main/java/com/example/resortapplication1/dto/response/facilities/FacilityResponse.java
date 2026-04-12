package com.example.resortapplication1.dto.response.facilities;

import com.example.resortapplication1.model.dto.FacilityDto;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class FacilityResponse {
    private FacilityDto data;

    public FacilityResponse(FacilityDto facility) {
        this.data = facility;
    }
}
