package com.example.resortbackendapplication1.resort.dto.request.resortroomcategoryprice;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Shared shape for HOLIDAY/SPECIAL price creation — always date-bound and never tied to days of week,
 * unlike BASE/WEEKDAY/WEEKEND (see {@link CreateResortRoomCategoryPriceGroupRequest}).
 */
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResortRoomCategoryDateBoundPriceRequest {

    @NotNull
    private Long currencyId;

    @NotNull
    private Long priceUnitId;

    @NotBlank
    @Size(max = 200)
    private String name;

    private String description;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal price;

    @NotNull
    private LocalDate validFrom;

    @NotNull
    private LocalDate validTo;

    /**
     * Tie-breaker when multiple HOLIDAY/SPECIAL rules could apply to the same date. Nullable, defaults to 0.
     */
    private Integer priority;
}
