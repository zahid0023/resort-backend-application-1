package com.example.resortbackendapplication1.resort.core.dto.request.resortbasicinfo.locale;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResortBasicInfoLocaleRequest {

    @NotNull
    private Integer sortOrder;

    @NotBlank
    @Size(max = 255)
    private String name;

    @NotBlank
    private String tagline;

    @Size(max = 1024)
    private String shortDescription;
}
