package com.example.resortapplication1.dto.request.resorts;

import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResortRequest {
    private String name;
    private String description;
    private String address;
    private Long countryId;
    private Long cityId;
    private String contactEmail;
    private String contactPhone;
}
