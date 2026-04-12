package com.example.resortapplication1.dto.response.facilitygroups;

import com.example.resortapplication1.model.dto.FacilityGroupDto;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class FacilityGroupResponse {
    private FacilityGroupDto data;

    public FacilityGroupResponse(FacilityGroupDto facilityGroup) {
        this.data = facilityGroup;
    }
}
