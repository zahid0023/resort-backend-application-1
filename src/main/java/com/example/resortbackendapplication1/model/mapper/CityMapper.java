package com.example.resortbackendapplication1.model.mapper;

import com.example.resortbackendapplication1.dto.request.city.CityRequest;
import com.example.resortbackendapplication1.dto.request.city.CreateCityRequest;
import com.example.resortbackendapplication1.dto.request.city.UpdateCityRequest;
import com.example.resortbackendapplication1.dto.request.city.citylocale.CreateCityLocaleRequest;
import com.example.resortbackendapplication1.model.dto.CityDto;
import com.example.resortbackendapplication1.model.dto.CityLocaleDto;
import com.example.resortbackendapplication1.model.entity.CityEntity;
import com.example.resortbackendapplication1.model.entity.CityLocaleEntity;
import com.example.resortbackendapplication1.model.entity.CountryEntity;
import com.example.resortbackendapplication1.model.entity.LocaleEntity;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class CityMapper {

    public CityEntity create(CreateCityRequest request,
                             CountryEntity countryEntity,
                             Map<Long, LocaleEntity> localeEntityMap) {
        CityEntity entity = new CityEntity();
        entity.setCountryEntity(countryEntity);
        entity.setCode(request.getCode());
        applyCommonFields(entity, request);
        entity.setCityLocaleEntities(mapLocales(request.getLocales(), entity, localeEntityMap));
        return entity;
    }

    public void update(CityEntity entity, UpdateCityRequest request) {
        applyCommonFields(entity, request);
    }

    private void applyCommonFields(CityEntity entity, CityRequest request) {
        entity.setSortOrder(request.getSortOrder());
    }

    private Set<CityLocaleEntity> mapLocales(List<CreateCityLocaleRequest> locales,
                                             CityEntity entity,
                                             Map<Long, LocaleEntity> localeEntityMap) {
        return locales.stream()
                .map(locale -> CityLocaleMapper.create(locale, entity, localeEntityMap.get(locale.getLocaleId())))
                .collect(Collectors.toSet());
    }

    public CityDto toDto(CityEntity entity) {
        List<CityLocaleDto> cityLocaleDtos = entity.getCityLocaleEntities().stream()
                .map(CityLocaleMapper::toDto)
                .toList();

        return CityDto.builder()
                .id(entity.getId())
                .countryId(entity.getCountryEntity().getId())
                .code(entity.getCode())
                .sortOrder(entity.getSortOrder())
                .locales(cityLocaleDtos)
                .build();
    }
}
