package com.example.resortbackendapplication1.resort.dto.response;

import com.example.resortbackendapplication1.resort.model.dto.ResortFacilityGroupLocaleDto;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResortFacilityGroupLocaleResponse {
    private final ResortFacilityGroupLocaleDto data;

    public ResortFacilityGroupLocaleResponse(ResortFacilityGroupLocaleDto data) {
        this.data = data;
    }
}
