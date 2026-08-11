package com.example.resortbackendapplication1.price.model.mapper;

import com.example.resortbackendapplication1.price.dto.request.pricescope.locale.PriceScopeLocaleRequest;
import com.example.resortbackendapplication1.price.dto.request.pricescope.locale.UpdatePriceScopeLocaleRequest;
import com.example.resortbackendapplication1.price.model.dto.PriceScopeLocaleDto;
import com.example.resortbackendapplication1.price.model.entity.PriceScopeLocaleEntity;
import com.example.resortbackendapplication1.locale.model.mapper.LocaleMapper;
import lombok.experimental.UtilityClass;

@UtilityClass
public class PriceScopeLocaleMapper {

    public PriceScopeLocaleEntity create(PriceScopeLocaleRequest request) {
        PriceScopeLocaleEntity entity = new PriceScopeLocaleEntity();
        applyCommonFields(entity, request);
        return entity;
    }

    public void update(PriceScopeLocaleEntity entity, UpdatePriceScopeLocaleRequest request) {
        applyCommonFields(entity, request);
    }

    private void applyCommonFields(PriceScopeLocaleEntity entity, PriceScopeLocaleRequest request) {
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
        entity.setSortOrder(request.getSortOrder());
    }

    public PriceScopeLocaleDto toDto(PriceScopeLocaleEntity entity) {
        return PriceScopeLocaleDto.builder()
                .id(entity.getId())
                .locale(LocaleMapper.toDto(entity.getLocaleEntity()))
                .name(entity.getName())
                .description(entity.getDescription())
                .sortOrder(entity.getSortOrder())
                .build();
    }
}
