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
 * {@code mainInherited}/{@code specialsInherited} are independent — a room can override just its Main rate,
 * just its Special rates, both, or neither, for the same currency. {@code true} means the corresponding field
 * below ({@code main}/{@code specials}) is the room's category's data instead of the room's own; {@code false}
 * means it's the room's own override(s).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResortRoomPriceGroupDto {

    private CurrencyDto currency;

    private Boolean mainInherited;

    private Boolean specialsInherited;

    private ResortRoomMainPriceDto main;

    private List<ResortRoomDateRangePriceDto> specials;
}
