package com.example.resortbackendapplication1.address.model.mapper;

import com.example.resortbackendapplication1.address.dto.request.city.CityRequest;
import com.example.resortbackendapplication1.address.dto.request.city.CreateCityRequest;
import com.example.resortbackendapplication1.address.dto.request.city.UpdateCityRequest;
import com.example.resortbackendapplication1.address.model.dto.CityDto;
import com.example.resortbackendapplication1.address.model.entity.CityEntity;
import com.example.resortbackendapplication1.address.model.entity.CityLocaleEntity;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class CityMapper {

    public CityEntity create(CreateCityRequest request) {
        CityEntity entity = new CityEntity();
        entity.setCode(request.getCode());
        applyCommonFields(entity, request);
        return entity;
    }

    public void update(CityEntity entity, UpdateCityRequest request) {
        applyCommonFields(entity, request);
    }

    private void applyCommonFields(CityEntity entity, CityRequest request) {
        entity.setSortOrder(request.getSortOrder());
    }

    public CityDto toDto(CityEntity entity) {
        return CityDto.builder()
                .id(entity.getId())
                .country(CountryMapper.toDto(entity.getCountryEntity()))
                .code(entity.getCode())
                .sortOrder(entity.getSortOrder())
                .locales(entity.getCityLocaleEntities().stream()
                        .map(CityLocaleMapper::toDto)
                        .toList())
                .build();
    }

    public CityDto toDto(CityEntity entity, Long localeId) {
        CityLocaleEntity matched = entity.getCityLocaleEntities().stream()
                .filter(cityLocaleEntity -> cityLocaleEntity.getLocaleEntity().getId().equals(localeId))
                .findFirst()
                .orElseGet(() -> entity.getCityLocaleEntities().stream()
                        .filter(cityLocaleEntity -> "en".equals(cityLocaleEntity.getLocaleEntity().getCode()))
                        .findFirst()
                        .orElse(null));

        return CityDto.builder()
                .id(entity.getId())
                .country(CountryMapper.toDto(entity.getCountryEntity(), localeId))
                .code(entity.getCode())
                .sortOrder(entity.getSortOrder())
                .locales(matched == null ? List.of() : List.of(CityLocaleMapper.toDto(matched)))
                .build();
    }
}
