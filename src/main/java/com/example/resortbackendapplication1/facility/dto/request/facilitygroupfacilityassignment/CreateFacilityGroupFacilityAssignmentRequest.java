package com.example.resortbackendapplication1.facility.dto.request.facilitygroupfacilityassignment;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CreateFacilityGroupFacilityAssignmentRequest {

    @NotNull
    private Long facilityId;

}
