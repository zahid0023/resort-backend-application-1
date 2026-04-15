package com.example.resortbackendapplication1.dto.request.resortfacilities;

import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BulkCreateResortFacilityRequest {
    private List<CreateResortFacilityRequest> facilities;
}
