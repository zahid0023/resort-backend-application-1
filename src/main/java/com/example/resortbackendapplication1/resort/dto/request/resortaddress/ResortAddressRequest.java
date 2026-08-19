package com.example.resortbackendapplication1.resort.dto.request.resortaddress;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResortAddressRequest {

    @NotNull
    private Long countryId;

    @NotNull
    private Long cityId;

    @Size(max = 50)
    private String postalCode;

    private Double lat;

    private Double lon;
}
