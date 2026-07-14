package com.example.resortbackendapplication1.resort.dto.response;

import com.example.resortbackendapplication1.resort.model.dto.ResortImageDto;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResortImageResponse {
    private final ResortImageDto resortImage;

    public ResortImageResponse(ResortImageDto resortImage) {
        this.resortImage = resortImage;
    }
}
