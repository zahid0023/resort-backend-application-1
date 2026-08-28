package com.example.resortbackendapplication1.resort.roomcategory.model.dto;
import com.example.resortbackendapplication1.resort.core.model.dto.ResortWeeklyScheduleDayDto;

import com.example.resortbackendapplication1.currency.model.dto.CurrencyDto;
import com.example.resortbackendapplication1.price.model.dto.PriceUnitDto;
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

/**
 * A resort room category's SPECIAL price row — a date-ranged rule with its own weekday/weekend price. There is
 * no separate holiday concept; a holiday is just a special price whose {@code name} says so (e.g.
 * "Eid-ul-Fitr"). See {@link ResortRoomCategoryPriceGroupDto#getSpecials()}.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResortRoomCategoryDateRangePriceDto {

    private Long id;

    private ResortRoomCategoryDto resortRoomCategory;

    private PriceUnitDto priceUnit;

    private CurrencyDto currency;

    private String name;

    private String description;

    private LocalDate validFrom;

    private LocalDate validTo;

    private BigDecimal weekdayPrice;

    private BigDecimal weekendPrice;

    private Integer priority;

    private List<ResortWeeklyScheduleDayDto> weekdayDays;

    private List<ResortWeeklyScheduleDayDto> weekendDays;
}
