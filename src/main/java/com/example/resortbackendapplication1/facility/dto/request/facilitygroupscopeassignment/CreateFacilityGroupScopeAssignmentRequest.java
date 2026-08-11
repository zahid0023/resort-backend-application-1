package com.example.resortbackendapplication1.facility.dto.request.facilitygroupscopeassignment;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CreateFacilityGroupScopeAssignmentRequest {

    @NotNull
    private Long facilityScopeId;

}
