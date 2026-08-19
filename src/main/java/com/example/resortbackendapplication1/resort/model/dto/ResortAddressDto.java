package com.example.resortbackendapplication1.resort.model.dto;

import com.example.resortbackendapplication1.address.model.dto.CityDto;
import com.example.resortbackendapplication1.address.model.dto.CountryDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResortAddressDto {

    private Long id;

    private CountryDto country;

    private CityDto city;

    private String postalCode;

    private Double lat;

    private Double lon;

    private ResortAddressLocaleDto locale;
}
