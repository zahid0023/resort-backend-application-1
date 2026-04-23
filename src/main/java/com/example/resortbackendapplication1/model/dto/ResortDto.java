package com.example.resortbackendapplication1.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.util.UUID;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResortDto {
    private Long id;
    private UUID uuid;
    private String name;
    private String description;
    private String address;
    private Long countryId;
    private Long cityId;
    private String contactEmail;
    private String contactPhone;
    private String displayImageUrl;
}
