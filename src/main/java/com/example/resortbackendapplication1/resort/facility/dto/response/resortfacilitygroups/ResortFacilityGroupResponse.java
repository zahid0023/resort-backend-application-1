package com.example.resortbackendapplication1.resort.facility.dto.response.resortfacilitygroups;

import com.example.resortbackendapplication1.resort.facility.model.dto.ResortFacilityGroupDto;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResortFacilityGroupResponse {

    private final ResortFacilityGroupDto data;

    public ResortFacilityGroupResponse(ResortFacilityGroupDto data) {
        this.data = data;
    }
}
