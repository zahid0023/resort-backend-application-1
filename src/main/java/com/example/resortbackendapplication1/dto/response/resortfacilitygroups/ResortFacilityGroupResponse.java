package com.example.resortbackendapplication1.dto.response.resortfacilitygroups;

import com.example.resortbackendapplication1.model.dto.ResortFacilityGroupDto;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResortFacilityGroupResponse {
    private ResortFacilityGroupDto data;

    public ResortFacilityGroupResponse(ResortFacilityGroupDto resortFacilityGroup) {
        this.data = resortFacilityGroup;
    }
}
