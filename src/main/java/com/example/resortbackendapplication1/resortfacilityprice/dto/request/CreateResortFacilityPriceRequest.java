package com.example.resortbackendapplication1.resortfacilityprice.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@EqualsAndHashCode(callSuper = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CreateResortFacilityPriceRequest extends ResortFacilityPriceRequest {

    @NotNull
    private Long currencyId;
}
