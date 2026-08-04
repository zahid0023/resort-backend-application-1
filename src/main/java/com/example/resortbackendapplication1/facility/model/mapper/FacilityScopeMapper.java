package com.example.resortbackendapplication1.facility.model.mapper;

import com.example.resortbackendapplication1.commons.context.LocaleContext;
import com.example.resortbackendapplication1.facility.dto.request.facilityscope.CreateFacilityScopeRequest;
import com.example.resortbackendapplication1.facility.dto.request.facilityscope.FacilityScopeRequest;
import com.example.resortbackendapplication1.facility.dto.request.facilityscope.UpdateFacilityScopeRequest;
import com.example.resortbackendapplication1.facility.model.dto.FacilityScopeDto;
import com.example.resortbackendapplication1.facility.model.dto.FacilityScopeLocaleDto;
import com.example.resortbackendapplication1.facility.model.entity.FacilityScopeEntity;
import com.example.resortbackendapplication1.facility.model.entity.FacilityScopeLocaleEntity;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class FacilityScopeMapper {

    public FacilityScopeEntity create(CreateFacilityScopeRequest request) {
        FacilityScopeEntity entity = new FacilityScopeEntity();
        entity.setCode(request.getCode());
        applyCommonFields(entity, request);
        return entity;
    }

    public void update(FacilityScopeEntity entity, UpdateFacilityScopeRequest request) {
        applyCommonFields(entity, request);
    }

    private void applyCommonFields(FacilityScopeEntity entity, FacilityScopeRequest request) {
        entity.setSortOrder(request.getSortOrder());
    }

    public FacilityScopeDto.FacilityScopeDtoBuilder toDto(FacilityScopeEntity entity) {
        return FacilityScopeDto.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .sortOrder(entity.getSortOrder())
                .locale(singleLocale(entity));
    }

    private FacilityScopeLocaleDto singleLocale(FacilityScopeEntity entity) {
        FacilityScopeLocaleEntity matched = matchLocale(entity, LocaleContext.getLocaleId());
        return matched == null ? null : FacilityScopeLocaleMapper.toDto(matched);
    }

    private List<FacilityScopeLocaleEntity> activeLocales(FacilityScopeEntity entity) {
        return entity.getFacilityScopeLocaleEntities().stream()
                .filter(facilityScopeLocaleEntity -> Boolean.TRUE.equals(facilityScopeLocaleEntity.getIsActive())
                        && Boolean.FALSE.equals(facilityScopeLocaleEntity.getIsDeleted()))
                .toList();
    }

    private FacilityScopeLocaleEntity matchLocale(FacilityScopeEntity entity, Long localeId) {
        List<FacilityScopeLocaleEntity> activeLocales = activeLocales(entity);
        return activeLocales.stream()
                .filter(facilityScopeLocaleEntity -> facilityScopeLocaleEntity.getLocaleEntity().getId().equals(localeId))
                .findFirst()
                .orElseGet(() -> activeLocales.stream()
                        .filter(facilityScopeLocaleEntity -> "en".equals(facilityScopeLocaleEntity.getLocaleEntity().getCode()))
                        .findFirst()
                        .orElse(null));
    }
}
