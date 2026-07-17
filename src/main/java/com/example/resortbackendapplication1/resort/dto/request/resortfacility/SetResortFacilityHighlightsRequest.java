package com.example.resortbackendapplication1.resort.dto.request.resortfacility;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.util.Set;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SetResortFacilityHighlightsRequest {

    @NotNull
    @Size(min = 4, max = 9, message = "You must highlight between 4 and 9 resort facilities")
    private Set<Long> facilityIds;
}
