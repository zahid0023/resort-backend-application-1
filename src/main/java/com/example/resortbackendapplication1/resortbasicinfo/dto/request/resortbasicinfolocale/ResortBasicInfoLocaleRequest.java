package com.example.resortbackendapplication1.resortbasicinfo.dto.request.resortbasicinfolocale;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResortBasicInfoLocaleRequest {

    @NotBlank
    @Size(max = 255)
    private String name;

    @Size(max = 1024)
    private String shortDescription;

    private String address;

    @NotNull
    private Integer sortOrder;
}
