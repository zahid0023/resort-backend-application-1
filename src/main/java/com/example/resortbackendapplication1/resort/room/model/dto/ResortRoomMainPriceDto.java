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
import java.util.List;

/**
 * {@code resortRoom} is {@code null} when this bundle is inherited from the room's category (see
 * {@link ResortRoomPriceGroupDto#getMainInherited()}) — an inherited row has no resort-room-owned id of its own.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResortRoomMainPriceDto {

    private Long id;

    private ResortRoomDto resortRoom;

    private PriceUnitDto priceUnit;

    private CurrencyDto currency;

    private BigDecimal basePrice;

    private BigDecimal weekdayPrice;

    private BigDecimal weekendPrice;

    private List<ResortWeeklyScheduleDayDto> weekdayDays;

    private List<ResortWeeklyScheduleDayDto> weekendDays;
}
