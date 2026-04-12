package com.example.resortbackendapplication1.dto.response.cities;

import com.example.resortbackendapplication1.model.dto.CityDto;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CityResponse {
    private CityDto data;

    public CityResponse(CityDto city) {
        this.data = city;
    }
}
