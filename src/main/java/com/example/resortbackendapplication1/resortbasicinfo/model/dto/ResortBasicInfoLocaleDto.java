package com.example.resortbackendapplication1.resortbasicinfo.model.dto;

import com.example.resortbackendapplication1.locale.model.dto.LocaleDto;
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
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResortBasicInfoLocaleDto {
    private Long id;
    private LocaleDto locale;
    private Integer sortOrder;
    private String name;
    private String tagline;
    private String shortDescription;
    private String address;
}
