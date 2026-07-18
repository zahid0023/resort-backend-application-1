package com.example.resortbackendapplication1.price.model.dto;

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
public class PriceTypeLocaleDto {
    private Long id;
    private LocaleDto locale;
    private String name;
    private String description;
    private Integer sortOrder;
    private String purpose;
    private String usageExample;
}
