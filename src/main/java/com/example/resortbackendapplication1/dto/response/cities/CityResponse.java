package com.example.resortbackendapplication1.dto.response.cities;

import com.example.resortbackendapplication1.model.dto.CityDto;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CityResponse {
    private final CityDto city;

    public CityResponse(CityDto city) {
        this.city = city;
    }
}
