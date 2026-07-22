package com.example.resortbackendapplication1.facility.dto.response.facilityscopes;

import com.example.resortbackendapplication1.facility.model.dto.FacilityScopeDto;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class FacilityScopeResponse {
    private final FacilityScopeDto data;

    public FacilityScopeResponse(FacilityScopeDto facilityScope) {
        this.data = facilityScope;
    }
}
