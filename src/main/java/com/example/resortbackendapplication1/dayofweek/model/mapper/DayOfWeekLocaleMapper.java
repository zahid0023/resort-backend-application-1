package com.example.resortbackendapplication1.dayofweek.model.mapper;

import com.example.resortbackendapplication1.dayofweek.dto.request.dayofweek.locale.DayOfWeekLocaleRequest;
import com.example.resortbackendapplication1.dayofweek.dto.request.dayofweek.locale.UpdateDayOfWeekLocaleRequest;
import com.example.resortbackendapplication1.dayofweek.model.dto.DayOfWeekLocaleDto;
import com.example.resortbackendapplication1.dayofweek.model.entity.DayOfWeekLocaleEntity;
import com.example.resortbackendapplication1.locale.model.mapper.LocaleMapper;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DayOfWeekLocaleMapper {

    public DayOfWeekLocaleEntity create(DayOfWeekLocaleRequest request) {
        DayOfWeekLocaleEntity entity = new DayOfWeekLocaleEntity();
        applyCommonFields(entity, request);
        return entity;
    }

    public void update(DayOfWeekLocaleEntity entity, UpdateDayOfWeekLocaleRequest request) {
        applyCommonFields(entity, request);
    }

    private void applyCommonFields(DayOfWeekLocaleEntity entity, DayOfWeekLocaleRequest request) {
        entity.setName(request.getName());
        entity.setShortName(request.getShortName());
        entity.setDescription(request.getDescription());
        entity.setSortOrder(request.getSortOrder());
    }

    public DayOfWeekLocaleDto toDto(DayOfWeekLocaleEntity entity) {
        return DayOfWeekLocaleDto.builder()
                .id(entity.getId())
                .locale(LocaleMapper.toDto(entity.getLocaleEntity()))
                .name(entity.getName())
                .shortName(entity.getShortName())
                .description(entity.getDescription())
                .sortOrder(entity.getSortOrder())
                .build();
    }
}
