package com.example.resortbackendapplication1.dto.response.resortimages;

import com.example.resortbackendapplication1.model.dto.ResortImageDto;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResortImageResponse {
    private ResortImageDto data;

    public ResortImageResponse(ResortImageDto data) {
        this.data = data;
    }
}
