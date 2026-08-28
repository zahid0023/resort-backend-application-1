package com.example.resortbackendapplication1.resort.room.model.dto;
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
 * A resort room's SPECIAL price override row — a date-ranged rule with its own weekday/weekend price. There is
 * no separate holiday concept; a holiday is just a special price whose {@code name} says so (e.g.
 * "Eid-ul-Fitr"). {@code resortRoom} is {@code null} when this bundle is inherited from the room's category.
 * See {@link ResortRoomPriceGroupDto#getSpecials()}.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResortRoomDateRangePriceDto {

    private Long id;

    private ResortRoomDto resortRoom;

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
