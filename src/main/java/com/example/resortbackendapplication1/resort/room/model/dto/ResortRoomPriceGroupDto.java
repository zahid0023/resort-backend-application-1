package com.example.resortbackendapplication1.resort.room.model.dto;

import com.example.resortbackendapplication1.currency.model.dto.CurrencyDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.util.List;

/**
 * {@code inherited} is {@code true} when the room has no active {@code main} override for {@link #currency} of
 * its own, so {@code main}/{@code specials} below are the room's category's bundle for this currency instead —
 * {@code false} means every field below is the room's own override.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResortRoomPriceGroupDto {

    private CurrencyDto currency;

    private Boolean inherited;

    private ResortRoomMainPriceDto main;

    private List<ResortRoomDateRangePriceDto> specials;
}
