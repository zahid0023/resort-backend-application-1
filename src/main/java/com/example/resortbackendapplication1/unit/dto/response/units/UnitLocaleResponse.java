package com.example.resortbackendapplication1.unit.dto.response.units;

import com.example.resortbackendapplication1.unit.model.dto.UnitLocaleDto;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UnitLocaleResponse {
    private final UnitLocaleDto unitLocale;

    public UnitLocaleResponse(UnitLocaleDto unitLocale) {
        this.unitLocale = unitLocale;
    }
}
