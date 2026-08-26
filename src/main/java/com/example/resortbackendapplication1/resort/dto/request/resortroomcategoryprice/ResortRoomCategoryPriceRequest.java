package com.example.resortbackendapplication1.resort.dto.request.resortroomcategoryprice;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.math.BigDecimal;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResortRoomCategoryPriceRequest {

    @NotNull
    private Long priceUnitId;

    @NotBlank
    @Size(max = 200)
    private String name;

    private String description;

    /** Matches the {@code numeric(12,2)} column — at most 10 integer digits and 2 fraction digits. */
    @NotNull
    @DecimalMin(value = "0")
    @Digits(integer = 10, fraction = 2)
    private BigDecimal price;

}
