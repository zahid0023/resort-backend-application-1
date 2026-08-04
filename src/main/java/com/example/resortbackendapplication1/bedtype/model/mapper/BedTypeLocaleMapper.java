package com.example.resortbackendapplication1.bedtype.model.mapper;

import com.example.resortbackendapplication1.bedtype.dto.request.bedtype.locale.BedTypeLocaleRequest;
import com.example.resortbackendapplication1.bedtype.dto.request.bedtype.locale.UpdateBedTypeLocaleRequest;
import com.example.resortbackendapplication1.bedtype.model.dto.BedTypeLocaleDto;
import com.example.resortbackendapplication1.bedtype.model.entity.BedTypeLocaleEntity;
import com.example.resortbackendapplication1.locale.model.mapper.LocaleMapper;
import lombok.experimental.UtilityClass;

@UtilityClass
public class BedTypeLocaleMapper {

    public BedTypeLocaleEntity create(BedTypeLocaleRequest request) {
        BedTypeLocaleEntity entity = new BedTypeLocaleEntity();
        applyCommonFields(entity, request);
        return entity;
    }

    public void update(BedTypeLocaleEntity entity, UpdateBedTypeLocaleRequest request) {
        applyCommonFields(entity, request);
    }

    private void applyCommonFields(BedTypeLocaleEntity entity, BedTypeLocaleRequest request) {
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
        entity.setSortOrder(request.getSortOrder());
    }

    public BedTypeLocaleDto toDto(BedTypeLocaleEntity entity) {
        return BedTypeLocaleDto.builder()
                .id(entity.getId())
                .locale(LocaleMapper.toDto(entity.getLocaleEntity()))
                .name(entity.getName())
                .description(entity.getDescription())
                .sortOrder(entity.getSortOrder())
                .build();
    }
}
