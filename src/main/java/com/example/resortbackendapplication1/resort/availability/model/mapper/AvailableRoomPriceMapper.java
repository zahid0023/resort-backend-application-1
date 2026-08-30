package com.example.resortbackendapplication1.resort.availability.model.mapper;

import com.example.resortbackendapplication1.currency.model.entity.CurrencyEntity;
import com.example.resortbackendapplication1.currency.model.mapper.CurrencyMapper;
import com.example.resortbackendapplication1.price.model.mapper.PriceUnitMapper;
import com.example.resortbackendapplication1.resort.availability.model.dto.AvailableRoomPriceDto;
import com.example.resortbackendapplication1.resort.availability.model.dto.NightlyRateDto;
import com.example.resortbackendapplication1.resort.pricing.RoomPricingResolver;
import lombok.experimental.UtilityClass;

@UtilityClass
public class AvailableRoomPriceMapper {

    public AvailableRoomPriceDto toDto(RoomPricingResolver.NightlyResult nightlyResult, CurrencyEntity currencyEntity) {
        return AvailableRoomPriceDto.builder()
                .currency(CurrencyMapper.toDto(currencyEntity).build())
                .priceUnit(PriceUnitMapper.toDto(nightlyResult.priceUnitEntity()).build())
                .nights(nightlyResult.nights().stream()
                        .map(nightlyRate -> NightlyRateDto.builder()
                                .date(nightlyRate.date())
                                .price(nightlyRate.price())
                                .rateType(nightlyRate.rateType())
                                .build())
                        .toList())
                .total(nightlyResult.totalPrice())
                .build();
    }
}
