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
import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResortRoomCategoryPriceRequest {

    @NotBlank
    @Size(max = 200)
    private String name;

    private String description;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal price;

    /**
     * Required for HOLIDAY/SPECIAL, forbidden for BASE/WEEKDAY/WEEKEND — enforced server-side.
     */
    private LocalDate validFrom;

    private LocalDate validTo;

    /**
     * Ignored (forced to 0) for BASE/WEEKDAY/WEEKEND — only meaningful for HOLIDAY/SPECIAL.
     */
    private Integer priority;

    /**
     * Required, non-empty for WEEKDAY/WEEKEND, forbidden for BASE/HOLIDAY/SPECIAL — enforced server-side.
     */
    private List<Long> dayOfWeekIds;

}
