package com.example.resortbackendapplication1.price.model.mapper;

import com.example.resortbackendapplication1.price.dto.request.pricetypescope.locale.PriceTypeScopeLocaleRequest;
import com.example.resortbackendapplication1.price.dto.request.pricetypescope.locale.UpdatePriceTypeScopeLocaleRequest;
import com.example.resortbackendapplication1.price.model.dto.PriceTypeScopeLocaleDto;
import com.example.resortbackendapplication1.price.model.entity.PriceTypeScopeLocaleEntity;
import com.example.resortbackendapplication1.locale.model.mapper.LocaleMapper;
import lombok.experimental.UtilityClass;

@UtilityClass
public class PriceTypeScopeLocaleMapper {

    public PriceTypeScopeLocaleEntity create(PriceTypeScopeLocaleRequest request) {
        PriceTypeScopeLocaleEntity entity = new PriceTypeScopeLocaleEntity();
        applyCommonFields(entity, request);
        return entity;
    }

    public void update(PriceTypeScopeLocaleEntity entity, UpdatePriceTypeScopeLocaleRequest request) {
        applyCommonFields(entity, request);
    }

    private void applyCommonFields(PriceTypeScopeLocaleEntity entity, PriceTypeScopeLocaleRequest request) {
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
        entity.setSortOrder(request.getSortOrder());
    }

    public PriceTypeScopeLocaleDto toDto(PriceTypeScopeLocaleEntity entity) {
        return PriceTypeScopeLocaleDto.builder()
                .id(entity.getId())
                .locale(LocaleMapper.toDto(entity.getLocaleEntity()))
                .name(entity.getName())
                .description(entity.getDescription())
                .sortOrder(entity.getSortOrder())
                .build();
    }
}
