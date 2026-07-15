package com.example.resortbackendapplication1.resort.dto.request.resortfacilitygroup;

import lombok.Data;
import lombok.EqualsAndHashCode;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@EqualsAndHashCode(callSuper = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UpdateResortFacilityGroupRequest extends ResortFacilityGroupRequest {
}
