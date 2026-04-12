package com.example.resortapplication1.model.mapper;

import com.example.resortapplication1.dto.request.templatepages.CreateTemplatePageRequest;
import com.example.resortapplication1.dto.request.templatepages.UpdateTemplatePageRequest;
import com.example.resortapplication1.model.dto.TemplatePageDto;
import com.example.resortapplication1.model.entity.PageTypeEntity;
import com.example.resortapplication1.model.entity.TemplateEntity;
import com.example.resortapplication1.model.entity.TemplatePageEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TemplatePageMapper {

    public static TemplatePageEntity fromRequest(CreateTemplatePageRequest request,
                                                 TemplateEntity templateEntity,
                                                 PageTypeEntity pageTypeEntity) {
        TemplatePageEntity entity = new TemplatePageEntity();
        entity.setTemplateEntity(templateEntity);
        entity.setPageTypeEntity(pageTypeEntity);
        entity.setPageName(request.getPageName());
        entity.setPageSlug(request.getPageSlug());
        entity.setPageOrder(request.getPageOrder());
        return entity;
    }

    public static void updateEntity(TemplatePageEntity entity,
                                    UpdateTemplatePageRequest request) {
        if (request.getPageName() != null) entity.setPageName(request.getPageName());
        if (request.getPageSlug() != null) entity.setPageSlug(request.getPageSlug());
        if (request.getPageOrder() != null) entity.setPageOrder(request.getPageOrder());
    }

    public static TemplatePageDto toDto(TemplatePageEntity entity) {
        return TemplatePageDto.builder()
                .id(entity.getId())
                .templateId(entity.getTemplateEntity().getId())
                .pageTypeId(entity.getPageTypeEntity().getId())
                .pageName(entity.getPageName())
                .pageSlug(entity.getPageSlug())
                .pageOrder(entity.getPageOrder())
                .templatePageSlots(entity.getTemplatePageSlotEntities().stream()
                        .map(TemplatePageSlotMapper::toDto)
                        .toList())
                .build();
    }
}
