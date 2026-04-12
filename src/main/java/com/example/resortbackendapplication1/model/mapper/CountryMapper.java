package com.example.resortbackendapplication1.model.mapper;

import com.example.resortbackendapplication1.dto.request.countries.CreateCountryRequest;
import com.example.resortbackendapplication1.dto.request.countries.UpdateCountryRequest;
import com.example.resortbackendapplication1.model.dto.CountryDto;
import com.example.resortbackendapplication1.model.entity.CountryEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CountryMapper {

    public static CountryEntity fromRequest(CreateCountryRequest request) {
        CountryEntity entity = new CountryEntity();
        entity.setCode(request.getCode());
        entity.setName(request.getName());
        return entity;
    }

    public static void updateEntity(CountryEntity entity, UpdateCountryRequest request) {
        if (request.getCode() != null) entity.setCode(request.getCode());
        if (request.getName() != null) entity.setName(request.getName());
    }

    public static CountryDto toDto(CountryEntity entity) {
        return CountryDto.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .name(entity.getName())
                .build();
    }
}
