package com.example.resortbackendapplication1.resortroomcategoryprice.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResortRoomCategoryPriceRequest {

    @NotNull
    private Long priceTypeId;

    @NotNull
    private Long priceUnitId;

    @NotNull
    @DecimalMin(value = "0.00", message = "amount must be 0 or greater")
    private BigDecimal amount;

    @NotNull
    private Integer priority;

    private LocalDate validFrom;

    private LocalDate validTo;

    @NotNull
    private Boolean monday;

    @NotNull
    private Boolean tuesday;

    @NotNull
    private Boolean wednesday;

    @NotNull
    private Boolean thursday;

    @NotNull
    private Boolean friday;

    @NotNull
    private Boolean saturday;

    @NotNull
    private Boolean sunday;
}
