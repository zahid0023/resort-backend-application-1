package com.example.resortbackendapplication1.resortfacilityprice.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.math.BigDecimal;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResortFacilityPriceRequest {

    @NotNull
    private Long priceUnitId;

    @NotNull
    @DecimalMin(value = "0.00", message = "amount must be 0 or greater")
    private BigDecimal amount;

    private String notes;

    @NotNull
    @Min(value = 0, message = "sort_order must be 0 or greater")
    private Integer sortOrder;
}
