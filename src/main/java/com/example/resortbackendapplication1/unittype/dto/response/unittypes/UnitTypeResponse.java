package com.example.resortbackendapplication1.unittype.dto.response.unittypes;

import com.example.resortbackendapplication1.unittype.model.dto.UnitTypeDto;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UnitTypeResponse {
    private final UnitTypeDto unitType;

    public UnitTypeResponse(UnitTypeDto unitType) {
        this.unitType = unitType;
    }
}
