package com.example.resortapplication1.dto.response.resortaccesstypes;

import com.example.resortapplication1.model.dto.ResortAccessTypeDto;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResortAccessTypeResponse {
    private ResortAccessTypeDto data;

    public ResortAccessTypeResponse(ResortAccessTypeDto resortAccessType) {
        this.data = resortAccessType;
    }
}
