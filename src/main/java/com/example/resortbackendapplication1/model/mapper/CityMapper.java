package com.example.resortbackendapplication1.model.mapper;

import com.example.resortbackendapplication1.dto.request.cities.CreateCityRequest;
import com.example.resortbackendapplication1.dto.request.cities.UpdateCityRequest;
import com.example.resortbackendapplication1.model.dto.CityDto;
import com.example.resortbackendapplication1.model.entity.CityEntity;
import com.example.resortbackendapplication1.model.entity.CountryEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CityMapper {

    public static CityEntity fromRequest(CreateCityRequest request, CountryEntity country) {
        CityEntity entity = new CityEntity();
        entity.setName(request.getName());
        entity.setCountryEntity(country);
        return entity;
    }

    public static void updateEntity(CityEntity entity, UpdateCityRequest request, CountryEntity country) {
        if (request.getName() != null) entity.setName(request.getName());
        if (country != null) entity.setCountryEntity(country);
    }

    public static CityDto toDto(CityEntity entity) {
        return CityDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .countryId(entity.getCountryEntity().getId())
                .build();
    }
}
