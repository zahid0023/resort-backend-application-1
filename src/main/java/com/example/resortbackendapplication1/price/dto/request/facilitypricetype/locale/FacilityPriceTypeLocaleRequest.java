package com.example.resortbackendapplication1.price.dto.request.resortfacilitypricetype.locale;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class FacilityPriceTypeLocaleRequest {

    @NotBlank
    @Size(max = 100)
    private String name;

    @NotNull
    private String description;

    @NotNull
    private Integer sortOrder;

    @NotNull
    private String purpose;

    @NotNull
    private String usageExample;

}
