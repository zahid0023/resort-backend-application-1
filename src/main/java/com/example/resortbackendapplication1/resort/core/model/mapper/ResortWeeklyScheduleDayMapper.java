package com.example.resortbackendapplication1.resort.core.model.mapper;

import com.example.resortbackendapplication1.resort.core.model.dto.ResortWeeklyScheduleDayDto;
import com.example.resortbackendapplication1.resort.core.model.entity.ResortWeeklyScheduleDayEntity;
import com.example.resortbackendapplication1.resort.core.model.enums.DayType;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ResortWeeklyScheduleDayMapper {

    public ResortWeeklyScheduleDayEntity create(DayType dayType) {
        ResortWeeklyScheduleDayEntity entity = new ResortWeeklyScheduleDayEntity();
        entity.setDayType(dayType);
        return entity;
    }

    public ResortWeeklyScheduleDayDto.ResortWeeklyScheduleDayDtoBuilder toDto(ResortWeeklyScheduleDayEntity entity) {
        return ResortWeeklyScheduleDayDto.builder()
                .id(entity.getId());
    }
}
