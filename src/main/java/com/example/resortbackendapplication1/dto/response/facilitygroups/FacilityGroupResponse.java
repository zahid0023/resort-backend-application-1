package com.example.resortbackendapplication1.dto.response.facilitygroups;

import com.example.resortbackendapplication1.model.dto.FacilityGroupDto;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class FacilityGroupResponse {
    private final FacilityGroupDto data;

    public FacilityGroupResponse(FacilityGroupDto facilityGroup) {
        this.data = facilityGroup;
    }
}
