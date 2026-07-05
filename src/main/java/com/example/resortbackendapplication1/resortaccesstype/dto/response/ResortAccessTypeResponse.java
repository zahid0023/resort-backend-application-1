package com.example.resortbackendapplication1.resortaccesstype.dto.response;

import com.example.resortbackendapplication1.resortaccesstype.model.dto.ResortAccessTypeDto;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResortAccessTypeResponse {
    private final ResortAccessTypeDto data;

    public ResortAccessTypeResponse(ResortAccessTypeDto resortAccessType) {
        this.data = resortAccessType;
    }
}
