package com.example.resortbackendapplication1.dayofweek.model.mapper;

import com.example.resortbackendapplication1.dayofweek.dto.request.dayofweek.dayofweeklocale.CreateDayOfWeekLocaleRequest;
import com.example.resortbackendapplication1.dayofweek.dto.request.dayofweek.dayofweeklocale.DayOfWeekLocaleRequest;
import com.example.resortbackendapplication1.dayofweek.dto.request.dayofweek.dayofweeklocale.UpdateDayOfWeekLocaleRequest;
import com.example.resortbackendapplication1.dayofweek.model.dto.DayOfWeekLocaleDto;
import com.example.resortbackendapplication1.dayofweek.model.entity.DayOfWeekEntity;
import com.example.resortbackendapplication1.dayofweek.model.entity.DayOfWeekLocaleEntity;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DayOfWeekLocaleMapper {

    public DayOfWeekLocaleEntity create(CreateDayOfWeekLocaleRequest request,
                                        DayOfWeekEntity dayOfWeekEntity,
                                        LocaleEntity localeEntity) {
        DayOfWeekLocaleEntity entity = new DayOfWeekLocaleEntity();
        entity.setDayOfWeekEntity(dayOfWeekEntity);
        entity.setLocaleEntity(localeEntity);
        applyCommonFields(entity, request);
        return entity;
    }

    public void update(DayOfWeekLocaleEntity entity, UpdateDayOfWeekLocaleRequest request) {
        applyCommonFields(entity, request);
    }

    private void applyCommonFields(DayOfWeekLocaleEntity entity, DayOfWeekLocaleRequest request) {
        entity.setName(request.getName());
        entity.setShortName(request.getShortName());
    }

    public DayOfWeekLocaleDto toDto(DayOfWeekLocaleEntity entity) {
        return DayOfWeekLocaleDto.builder()
                .id(entity.getId())
                .localeId(entity.getLocaleEntity().getId())
                .name(entity.getName())
                .shortName(entity.getShortName())
                .build();
    }
}
