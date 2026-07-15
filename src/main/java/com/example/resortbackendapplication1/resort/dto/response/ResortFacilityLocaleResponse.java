package com.example.resortbackendapplication1.resort.dto.response;

import com.example.resortbackendapplication1.resort.model.dto.ResortFacilityLocaleDto;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResortFacilityLocaleResponse {
    private final ResortFacilityLocaleDto data;

    public ResortFacilityLocaleResponse(ResortFacilityLocaleDto data) {
        this.data = data;
    }
}
