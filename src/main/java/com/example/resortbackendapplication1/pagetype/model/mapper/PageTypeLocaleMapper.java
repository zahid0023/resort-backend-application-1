package com.example.resortbackendapplication1.pagetype.model.mapper;

import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.pagetype.dto.request.pagetype.pagetypelocale.CreatePageTypeLocaleRequest;
import com.example.resortbackendapplication1.pagetype.dto.request.pagetype.pagetypelocale.PageTypeLocaleRequest;
import com.example.resortbackendapplication1.pagetype.dto.request.pagetype.pagetypelocale.UpdatePageTypeLocaleRequest;
import com.example.resortbackendapplication1.pagetype.model.dto.PageTypeLocaleDto;
import com.example.resortbackendapplication1.pagetype.model.entity.PageTypeEntity;
import com.example.resortbackendapplication1.pagetype.model.entity.PageTypeLocaleEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class PageTypeLocaleMapper {

    public PageTypeLocaleEntity create(CreatePageTypeLocaleRequest request,
                                       PageTypeEntity pageTypeEntity,
                                       LocaleEntity localeEntity) {
        PageTypeLocaleEntity entity = new PageTypeLocaleEntity();
        entity.setPageTypeEntity(pageTypeEntity);
        entity.setLocaleEntity(localeEntity);
        applyCommonFields(entity, request);
        return entity;
    }

    public void update(PageTypeLocaleEntity entity, UpdatePageTypeLocaleRequest request) {
        applyCommonFields(entity, request);
    }

    private void applyCommonFields(PageTypeLocaleEntity entity, PageTypeLocaleRequest request) {
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
        entity.setSortOrder(request.getSortOrder());
    }

    public PageTypeLocaleDto toDto(PageTypeLocaleEntity entity) {
        return PageTypeLocaleDto.builder()
                .id(entity.getId())
                .localeId(entity.getLocaleEntity().getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .sortOrder(entity.getSortOrder())
                .build();
    }
}
