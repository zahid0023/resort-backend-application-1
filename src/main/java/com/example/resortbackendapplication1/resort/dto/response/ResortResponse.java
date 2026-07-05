package com.example.resortbackendapplication1.resort.dto.response;

import com.example.resortbackendapplication1.resort.model.dto.ResortDto;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResortResponse {
    private final ResortDto data;

    public ResortResponse(ResortDto resort) {
        this.data = resort;
    }
}
