package com.example.resortbackendapplication1.dto.response.countries;

import com.example.resortbackendapplication1.model.dto.CountryDto;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CountryResponse {
    private final CountryDto country;

    public CountryResponse(CountryDto country) {
        this.country = country;
    }
}
