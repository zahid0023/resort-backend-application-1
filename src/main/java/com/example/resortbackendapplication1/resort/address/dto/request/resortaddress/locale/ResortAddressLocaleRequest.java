package com.example.resortbackendapplication1.resort.address.dto.request.resortaddress.locale;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResortAddressLocaleRequest {

    @NotBlank
    private String address;

    @NotNull
    private Integer sortOrder;
}
