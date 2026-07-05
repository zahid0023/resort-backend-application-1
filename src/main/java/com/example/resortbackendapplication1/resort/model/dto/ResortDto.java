package com.example.resortbackendapplication1.resort.model.dto;

import lombok.Builder;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResortDto {
    private Long id;
    private String code;
}
