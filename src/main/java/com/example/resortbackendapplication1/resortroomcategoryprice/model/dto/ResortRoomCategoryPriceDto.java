package com.example.resortbackendapplication1.resortroomcategoryprice.model.dto;

import com.example.resortbackendapplication1.currency.model.dto.CurrencyDto;
import com.example.resortbackendapplication1.price.model.dto.PriceTypeDto;
import com.example.resortbackendapplication1.price.model.dto.PriceUnitDto;
import com.example.resortbackendapplication1.resortroomcategory.model.dto.ResortRoomCategoryDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResortRoomCategoryPriceDto {
    private Long id;
    private ResortRoomCategoryDto resortRoomCategory;
    private PriceTypeDto priceType;
    private PriceUnitDto priceUnit;
    private CurrencyDto currency;
    private String name;
    private String description;
    private BigDecimal price;
    private LocalDate validFrom;
    private LocalDate validTo;
    private Integer priority;
    private List<Long> dayOfWeekIds;
    private BigDecimal discountAmount;
    private BigDecimal discountPercentage;
}
