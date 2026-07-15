package com.example.resortbackendapplication1.resort.dto.request.resortfacility.resortfacilitylocale;

import lombok.Data;
import lombok.EqualsAndHashCode;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UpdateResortFacilityLocaleRequest extends ResortFacilityLocaleRequest {
}
