package com.example.resortbackendapplication1.model.mapper;

import com.example.resortbackendapplication1.auth.model.enitty.UserEntity;
import com.example.resortbackendapplication1.commons.dto.request.ImageRequest;
import com.example.resortbackendapplication1.dto.request.resortimagestorageconfigs.CreateResortImageStorageConfigRequest;
import com.example.resortbackendapplication1.dto.request.resorts.CreateResortRequest;
import com.example.resortbackendapplication1.dto.request.resorts.UpdateResortRequest;
import com.example.resortbackendapplication1.model.dto.ResortDto;
import com.example.resortbackendapplication1.model.entity.*;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@UtilityClass
public class ResortMapper {

    public static ResortEntity fromRequest(CreateResortRequest request,
                                           UserEntity userEntity,
                                           ResortAccessTypeEntity resortAccessTypeEntity,
                                           CountryEntity countryEntity,
                                           CityEntity cityEntity,
                                           CreateResortImageStorageConfigRequest createResortImageStorageConfigRequest,
                                           List<ImageRequest> imageRequests) {
        ResortEntity entity = new ResortEntity();
        entity.setUserResortAccessesEntities(Set.of(toUserAccessEntity(entity, userEntity, resortAccessTypeEntity)));
        entity.setUuid(UUID.randomUUID());
        entity.setName(request.getName());
        entity.setDescription(request.getDescription() != null ? request.getDescription() : "");
        entity.setAddress(request.getAddress());
        entity.setCountryEntity(countryEntity);
        entity.setCityEntity(cityEntity);
        entity.setContactEmail(request.getContactEmail());
        entity.setContactPhone(request.getContactPhone());

        ResortImageStorageConfigEntity resortImageStorageConfigEntity = ResortImageStorageConfigMapper.fromRequest(createResortImageStorageConfigRequest, entity);
        entity.setResortImageStorageConfigEntities(Set.of(resortImageStorageConfigEntity));

        Set<ResortImageEntity> resortImageEntities = imageRequests.stream()
                .map(imageRequest -> ResortImageMapper.fromRequest(imageRequest, entity))
                .collect(Collectors.toSet());
        entity.setResortImageEntities(resortImageEntities);
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

    private static UserResortAccessEntity toUserAccessEntity(ResortEntity resortEntity,
                                                             UserEntity userEntity,
                                                             ResortAccessTypeEntity accessTypeEntity) {
        UserResortAccessEntity entity = new UserResortAccessEntity();
        entity.setResortEntity(resortEntity);
        entity.setUserEntity(userEntity);
        entity.setAccessTypeEntity(accessTypeEntity);
        return entity;
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
