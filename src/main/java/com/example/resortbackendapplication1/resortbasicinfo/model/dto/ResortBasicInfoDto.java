package com.example.resortbackendapplication1.resortbasicinfo.model.dto;

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
    private String code;
    private Integer sortOrder;
    private Short estd;
    private Long countryId;
    private Long cityId;
    private String phone;
    private String email;
    private String logoUrl;
    private Double lat;
    private Double lon;
    private List<ResortBasicInfoLocaleDto> locales;
}
