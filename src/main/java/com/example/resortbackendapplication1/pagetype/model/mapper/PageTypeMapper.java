package com.example.resortbackendapplication1.pagetype.model.mapper;

import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.pagetype.dto.request.pagetype.CreatePageTypeRequest;
import com.example.resortbackendapplication1.pagetype.dto.request.pagetype.PageTypeRequest;
import com.example.resortbackendapplication1.pagetype.dto.request.pagetype.UpdatePageTypeRequest;
import com.example.resortbackendapplication1.pagetype.dto.request.pagetype.pagetypelocale.CreatePageTypeLocaleRequest;
import com.example.resortbackendapplication1.pagetype.model.dto.PageTypeDto;
import com.example.resortbackendapplication1.pagetype.model.dto.PageTypeLocaleDto;
import com.example.resortbackendapplication1.pagetype.model.entity.PageTypeEntity;
import com.example.resortbackendapplication1.pagetype.model.entity.PageTypeLocaleEntity;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class PageTypeMapper {

    public PageTypeEntity create(CreatePageTypeRequest request,
                                 Map<Long, LocaleEntity> localeEntityMap) {
        PageTypeEntity entity = new PageTypeEntity();
        entity.setCode(request.getCode());
        applyCommonFields(entity, request);
        entity.setPageTypeLocaleEntities(mapLocales(request.getLocales(), entity, localeEntityMap));
        return entity;
    }

    public void update(PageTypeEntity entity, UpdatePageTypeRequest request) {
        applyCommonFields(entity, request);
    }

    private void applyCommonFields(PageTypeEntity entity, PageTypeRequest request) {
        entity.setSortOrder(request.getSortOrder());
    }

    private Set<PageTypeLocaleEntity> mapLocales(List<CreatePageTypeLocaleRequest> locales,
                                                  PageTypeEntity entity,
                                                  Map<Long, LocaleEntity> localeEntityMap) {
        return locales.stream()
                .map(locale -> PageTypeLocaleMapper.create(locale, entity, localeEntityMap.get(locale.getLocaleId())))
                .collect(Collectors.toSet());
    }

    public PageTypeDto toDto(PageTypeEntity entity) {
        List<PageTypeLocaleDto> locales = entity.getPageTypeLocaleEntities().stream()
                .map(PageTypeLocaleMapper::toDto)
                .toList();

        return PageTypeDto.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .sortOrder(entity.getSortOrder())
                .locales(locales)
                .build();
    }
}
