package com.example.resortbackendapplication1.resort.core.dto.response.resorts;

import com.example.resortbackendapplication1.resort.core.model.dto.ResortDto;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResortResponse {
    private final ResortDto data;

    public ResortResponse(ResortDto data) {
        this.data = data;
    }
}
