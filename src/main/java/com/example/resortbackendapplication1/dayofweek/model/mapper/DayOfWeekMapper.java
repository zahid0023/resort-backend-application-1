package com.example.resortbackendapplication1.dayofweek.model.mapper;

import com.example.resortbackendapplication1.dayofweek.dto.request.dayofweek.DayOfWeekRequest;
import com.example.resortbackendapplication1.dayofweek.dto.request.dayofweek.UpdateDayOfWeekRequest;
import com.example.resortbackendapplication1.dayofweek.model.dto.DayOfWeekDto;
import com.example.resortbackendapplication1.dayofweek.model.dto.DayOfWeekLocaleDto;
import com.example.resortbackendapplication1.dayofweek.model.entity.DayOfWeekEntity;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class DayOfWeekMapper {

    public void update(DayOfWeekEntity entity, UpdateDayOfWeekRequest request) {
        applyCommonFields(entity, request);
    }

    private void applyCommonFields(DayOfWeekEntity entity, DayOfWeekRequest request) {
        entity.setDisplayOrder(request.getDisplayOrder());
    }

    public DayOfWeekDto toDto(DayOfWeekEntity entity) {
        List<DayOfWeekLocaleDto> locales = entity.getDayOfWeekLocaleEntities().stream()
                .filter(locale -> Boolean.TRUE.equals(locale.getIsActive()) && Boolean.FALSE.equals(locale.getIsDeleted()))
                .map(DayOfWeekLocaleMapper::toDto)
                .toList();

        return DayOfWeekDto.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .isoDayNumber(entity.getIsoDayNumber())
                .displayOrder(entity.getDisplayOrder())
                .locales(locales)
                .build();
    }
}
