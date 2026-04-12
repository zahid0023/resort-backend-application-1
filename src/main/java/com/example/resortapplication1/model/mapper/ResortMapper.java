package com.example.resortapplication1.model.mapper;

import com.example.resortapplication1.dto.request.resorts.CreateResortRequest;
import com.example.resortapplication1.dto.request.resorts.UpdateResortRequest;
import com.example.resortapplication1.model.dto.ResortDto;
import com.example.resortapplication1.model.entity.CityEntity;
import com.example.resortapplication1.model.entity.CountryEntity;
import com.example.resortapplication1.model.entity.ResortEntity;
import lombok.experimental.UtilityClass;

import java.util.UUID;

@UtilityClass
public class ResortMapper {

    public static ResortEntity fromRequest(CreateResortRequest request, CountryEntity country, CityEntity city) {
        ResortEntity entity = new ResortEntity();
        entity.setUuid(UUID.randomUUID());
        entity.setName(request.getName());
        entity.setDescription(request.getDescription() != null ? request.getDescription() : "");
        entity.setAddress(request.getAddress());
        entity.setCountryEntity(country);
        entity.setCityEntity(city);
        entity.setContactEmail(request.getContactEmail());
        entity.setContactPhone(request.getContactPhone());
        return entity;
    }

    public static void updateEntity(ResortEntity entity, UpdateResortRequest request, CountryEntity country, CityEntity city) {
        if (request.getName() != null) entity.setName(request.getName());
        if (request.getDescription() != null) entity.setDescription(request.getDescription());
        if (request.getAddress() != null) entity.setAddress(request.getAddress());
        if (country != null) entity.setCountryEntity(country);
        if (city != null) entity.setCityEntity(city);
        if (request.getContactEmail() != null) entity.setContactEmail(request.getContactEmail());
        if (request.getContactPhone() != null) entity.setContactPhone(request.getContactPhone());
    }

    public static ResortDto toDto(ResortEntity entity) {
        return ResortDto.builder()
                .id(entity.getId())
                .uuid(entity.getUuid())
                .name(entity.getName())
                .description(entity.getDescription())
                .address(entity.getAddress())
                .countryId(entity.getCountryEntity().getId())
                .cityId(entity.getCityEntity() != null ? entity.getCityEntity().getId() : null)
                .contactEmail(entity.getContactEmail())
                .contactPhone(entity.getContactPhone())
                .build();
    }
}
