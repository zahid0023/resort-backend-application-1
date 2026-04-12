package com.example.resortapplication1.dto.request.resortaccesstypes;

import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResortAccessTypeRequest {
    private String code;
    private String name;
    private String description;
}
