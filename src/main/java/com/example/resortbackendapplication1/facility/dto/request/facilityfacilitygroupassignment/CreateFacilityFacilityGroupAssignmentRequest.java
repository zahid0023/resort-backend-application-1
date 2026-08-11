package com.example.resortbackendapplication1.facility.dto.request.facilityfacilitygroupassignment;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CreateFacilityFacilityGroupAssignmentRequest {

    @NotNull
    private Long facilityGroupId;

}
