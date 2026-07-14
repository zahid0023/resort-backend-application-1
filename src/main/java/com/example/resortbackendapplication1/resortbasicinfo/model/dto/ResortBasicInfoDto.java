package com.example.resortbackendapplication1.resortbasicinfo.model.dto;

import com.example.resortbackendapplication1.address.model.dto.CityDto;
import com.example.resortbackendapplication1.address.model.dto.CountryDto;
import com.example.resortbackendapplication1.resort.model.dto.ResortDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResortBasicInfoDto {
    private Long id;
    private ResortDto resort;
    private String code;
    private Integer sortOrder;
    private Short estd;
    private CountryDto country;
    private CityDto city;
    private String logoUrl;
    private Double lat;
    private Double lon;
    private List<ResortBasicInfoLocaleDto> locales;
}
