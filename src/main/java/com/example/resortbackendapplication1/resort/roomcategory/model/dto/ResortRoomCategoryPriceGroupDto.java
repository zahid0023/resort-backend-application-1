package com.example.resortbackendapplication1.resort.roomcategory.model.dto;

import com.example.resortbackendapplication1.currency.model.dto.CurrencyDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResortRoomCategoryPriceGroupDto {

    private CurrencyDto currency;

    private ResortRoomCategoryMainPriceDto main;

    private List<ResortRoomCategoryDateRangePriceDto> specials;
}
