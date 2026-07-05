package com.example.resortbackendapplication1.resortpermissiontype.dto.request.locale;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResortPermissionTypeLocaleRequest {

    @NotBlank
    @Size(max = 255)
    private String name;

    private String description;

    @NotNull
    @Min(1)
    private Integer sortOrder;
}
