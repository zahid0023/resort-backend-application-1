package com.example.resortbackendapplication1.resort.roomcategory.dto.request.resortroomcategoryprice;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Shape for SPECIAL price creation — always date-bound, with its own weekday/weekend price pair for the
 * range, unlike BASE/WEEKDAY/WEEKEND (see {@link ResortRoomCategoryMainPriceRequest}). There is no separate
 * holiday concept; a holiday is just a special price whose {@code name} says so (e.g. "Eid-ul-Fitr").
 */
@Data
@EqualsAndHashCode(callSuper = false)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResortRoomCategoryDateRangePriceRequest {

    @NotNull
    private Long priceUnitId;

    @NotBlank
    @Size(max = 200)
    private String name;

    private String description;

    @NotNull
    private LocalDate validFrom;

    @NotNull
    private LocalDate validTo;

    /** Applies on weekday dates within the range. No cap vs. the room category's base price. */
    @NotNull
    @DecimalMin(value = "0")
    @Digits(integer = 10, fraction = 2)
    private BigDecimal weekdayPrice;

    /** Applies on weekend dates within the range. No cap vs. the room category's base price. */
    @NotNull
    @DecimalMin(value = "0")
    @Digits(integer = 10, fraction = 2)
    private BigDecimal weekendPrice;

    /**
     * Tie-breaker when multiple SPECIAL rules could apply to the same date. Nullable, defaults to 0.
     */
    private Integer priority;
}
