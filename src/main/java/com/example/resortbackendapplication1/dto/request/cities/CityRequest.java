package com.example.resortbackendapplication1.dto.request.cities;

import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CityRequest {
    private String name;
    private Long countryId;
}
