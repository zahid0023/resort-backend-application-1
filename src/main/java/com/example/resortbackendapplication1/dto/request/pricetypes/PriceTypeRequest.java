package com.example.resortbackendapplication1.dto.request.pricetypes;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PriceTypeRequest {
    private String code;
    private String name;
    private String description;
}
