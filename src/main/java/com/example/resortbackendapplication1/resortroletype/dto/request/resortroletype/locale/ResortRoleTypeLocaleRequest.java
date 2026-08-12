package com.example.resortbackendapplication1.resortroletype.dto.request.resortroletype.locale;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResortRoleTypeLocaleRequest {

    @NotBlank
    @Size(max = 255)
    private String name;

    @NotNull
    private String description;

    @NotNull
    private Integer sortOrder;

}
