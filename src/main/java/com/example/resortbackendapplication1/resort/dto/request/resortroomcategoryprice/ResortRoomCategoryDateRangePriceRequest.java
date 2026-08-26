package com.example.resortbackendapplication1.resort.dto.request.resortroomcategoryprice;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.time.LocalDate;

/**
 * Shared shape for HOLIDAY/SPECIAL price creation — always date-bound and never tied to days of week,
 * unlike BASE/WEEKDAY/WEEKEND (see {@link ResortRoomCategoryMainPriceRequest}).
 */
@Data
@EqualsAndHashCode(callSuper = false)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResortRoomCategoryDateRangePriceRequest extends ResortRoomCategoryPriceRequest {
    @NotNull
    private LocalDate validFrom;

    @NotNull
    private LocalDate validTo;

    /**
     * Tie-breaker when multiple HOLIDAY/SPECIAL rules could apply to the same date. Nullable, defaults to 0.
     */
    private Integer priority;
}
