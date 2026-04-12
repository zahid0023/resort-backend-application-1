package com.example.resortbackendapplication1.dto.response.countries;

import com.example.resortbackendapplication1.model.dto.CountryDto;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CountryResponse {
    private CountryDto data;

    public CountryResponse(CountryDto country) {
        this.data = country;
    }
}
