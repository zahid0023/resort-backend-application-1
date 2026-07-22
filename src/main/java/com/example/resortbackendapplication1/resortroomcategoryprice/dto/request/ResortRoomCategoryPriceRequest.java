package com.example.resortbackendapplication1.resortroomcategoryprice.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResortRoomCategoryPriceRequest {

    @NotBlank
    @Size(max = 200)
    private String name;

    private String description;

    @NotNull
    @DecimalMin(value = "0.00", message = "price must be 0 or greater")
    private BigDecimal price;

    @NotNull
    private Long priceUnitId;

    // Required for WEEKDAY and WEEKEND; must be null/empty for BASE, HOLIDAY, SPECIAL
    private Set<Long> dayOfWeekIds;

    // Required for HOLIDAY and SPECIAL; must be null for BASE, WEEKDAY, WEEKEND
    private LocalDate validFrom;

    private LocalDate validTo;

    // Fixed for BASE (0), WEEKDAY (10), WEEKEND (20). User-settable for HOLIDAY (>=100) and SPECIAL (>=200).
    private Integer priority;
}
