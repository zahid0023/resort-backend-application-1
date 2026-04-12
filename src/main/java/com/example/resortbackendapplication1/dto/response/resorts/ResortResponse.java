package com.example.resortbackendapplication1.dto.response.resorts;

import com.example.resortbackendapplication1.model.dto.ResortDto;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResortResponse {
    private ResortDto data;

    public ResortResponse(ResortDto resort) {
        this.data = resort;
    }
}
