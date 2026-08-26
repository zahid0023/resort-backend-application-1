package com.example.resortbackendapplication1.resort.model.mapper;

import com.example.resortbackendapplication1.price.model.entity.PriceTypeEntity;
import com.example.resortbackendapplication1.resort.model.dto.ResortWeeklyScheduleDayDto;
import com.example.resortbackendapplication1.resort.model.entity.ResortWeeklyScheduleDayEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ResortWeeklyScheduleDayMapper {

    public ResortWeeklyScheduleDayEntity create(PriceTypeEntity priceTypeEntity) {
        ResortWeeklyScheduleDayEntity entity = new ResortWeeklyScheduleDayEntity();
        entity.setPriceTypeEntity(priceTypeEntity);
        return entity;
    }

    public ResortWeeklyScheduleDayDto.ResortWeeklyScheduleDayDtoBuilder toDto(ResortWeeklyScheduleDayEntity entity) {
        return ResortWeeklyScheduleDayDto.builder()
                .id(entity.getId());
    }
}
