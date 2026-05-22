package com.example.resortbackendapplication1.dto.response.facilities;

import com.example.resortbackendapplication1.model.dto.FacilityDto;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class FacilityResponse {
    private final FacilityDto data;

    public FacilityResponse(FacilityDto facility) {
        this.data = facility;
    }
}
