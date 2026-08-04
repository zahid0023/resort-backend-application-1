package com.example.resortbackendapplication1.dayofweek.model.mapper;

import com.example.resortbackendapplication1.commons.context.LocaleContext;
import com.example.resortbackendapplication1.dayofweek.model.dto.DayOfWeekDto;
import com.example.resortbackendapplication1.dayofweek.model.dto.DayOfWeekLocaleDto;
import com.example.resortbackendapplication1.dayofweek.model.entity.DayOfWeekEntity;
import com.example.resortbackendapplication1.dayofweek.model.entity.DayOfWeekLocaleEntity;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class DayOfWeekMapper {

    public DayOfWeekDto.DayOfWeekDtoBuilder toDto(DayOfWeekEntity entity) {
        return DayOfWeekDto.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .sortOrder(entity.getSortOrder())
                .locale(singleLocale(entity));
    }

    private List<DayOfWeekLocaleEntity> activeLocales(DayOfWeekEntity entity) {
        return entity.getDayOfWeekLocaleEntities().stream()
                .filter(dayOfWeekLocaleEntity -> Boolean.TRUE.equals(dayOfWeekLocaleEntity.getIsActive())
                        && Boolean.FALSE.equals(dayOfWeekLocaleEntity.getIsDeleted()))
                .toList();
    }

    private DayOfWeekLocaleDto singleLocale(DayOfWeekEntity entity) {
        DayOfWeekLocaleEntity matched = matchLocale(entity, LocaleContext.getLocaleId());
        return matched == null ? null : DayOfWeekLocaleMapper.toDto(matched);
    }

    private DayOfWeekLocaleEntity matchLocale(DayOfWeekEntity entity, Long localeId) {
        List<DayOfWeekLocaleEntity> activeLocales = activeLocales(entity);
        return activeLocales.stream()
                .filter(dayOfWeekLocaleEntity -> dayOfWeekLocaleEntity.getLocaleEntity().getId().equals(localeId))
                .findFirst()
                .orElseGet(() -> activeLocales.stream()
                        .filter(dayOfWeekLocaleEntity -> "en".equals(dayOfWeekLocaleEntity.getLocaleEntity().getCode()))
                        .findFirst()
                        .orElse(null));
    }
}
