package com.example.resortbackendapplication1.address.model.mapper;

import com.example.resortbackendapplication1.address.dto.request.city.locale.CityLocaleRequest;
import com.example.resortbackendapplication1.address.dto.request.city.locale.CreateCityLocaleRequest;
import com.example.resortbackendapplication1.address.dto.request.city.locale.UpdateCityLocaleRequest;
import com.example.resortbackendapplication1.address.model.dto.CityLocaleDto;
import com.example.resortbackendapplication1.address.model.entity.CityLocaleEntity;
import com.example.resortbackendapplication1.locale.model.mapper.LocaleMapper;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CityLocaleMapper {

    public CityLocaleEntity create(CreateCityLocaleRequest request) {
        CityLocaleEntity entity = new CityLocaleEntity();
        applyCommonFields(entity, request);
        return entity;
    }

    public void update(CityLocaleEntity entity, UpdateCityLocaleRequest request) {
        applyCommonFields(entity, request);
    }

    private void applyCommonFields(CityLocaleEntity entity, CityLocaleRequest request) {
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
        entity.setSortOrder(request.getSortOrder());
    }

    public CityLocaleDto toDto(CityLocaleEntity entity) {
        return CityLocaleDto.builder()
                .id(entity.getId())
                .locale(LocaleMapper.toDto(entity.getLocaleEntity()))
                .name(entity.getName())
                .description(entity.getDescription())
                .sortOrder(entity.getSortOrder())
                .build();
    }
}
