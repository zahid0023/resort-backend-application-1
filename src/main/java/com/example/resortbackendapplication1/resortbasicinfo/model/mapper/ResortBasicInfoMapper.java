package com.example.resortbackendapplication1.resortbasicinfo.model.mapper;

import com.example.resortbackendapplication1.address.model.entity.CityEntity;
import com.example.resortbackendapplication1.address.model.entity.CountryEntity;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.resort.model.entity.ResortEntity;
import com.example.resortbackendapplication1.resortbasicinfo.dto.request.resortbasicinfo.CreateResortBasicInfoRequest;
import com.example.resortbackendapplication1.resortbasicinfo.dto.request.resortbasicinfo.ResortBasicInfoRequest;
import com.example.resortbackendapplication1.resortbasicinfo.dto.request.resortbasicinfo.UpdateResortBasicInfoRequest;
import com.example.resortbackendapplication1.resortbasicinfo.dto.request.resortbasicinfolocale.CreateResortBasicInfoLocaleRequest;
import com.example.resortbackendapplication1.resortbasicinfo.model.dto.ResortBasicInfoDto;
import com.example.resortbackendapplication1.resortbasicinfo.model.dto.ResortBasicInfoLocaleDto;
import com.example.resortbackendapplication1.resortbasicinfo.model.entity.ResortBasicInfoEntity;
import com.example.resortbackendapplication1.resortbasicinfo.model.entity.ResortBasicInfoLocaleEntity;
import lombok.experimental.UtilityClass;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class ResortBasicInfoMapper {

    public ResortBasicInfoEntity create(CreateResortBasicInfoRequest request,
                                        ResortEntity resortEntity,
                                        CountryEntity countryEntity,
                                        CityEntity cityEntity,
                                        Map<Long, LocaleEntity> localeEntityMap) {
        ResortBasicInfoEntity entity = new ResortBasicInfoEntity();
        entity.setResortEntity(resortEntity);
        entity.setCode(request.getCode());
        applyCommonFields(entity, request, countryEntity, cityEntity);
        entity.setLocaleEntities(mapLocales(request.getLocales(), entity, localeEntityMap));
        return entity;
    }

    public void update(ResortBasicInfoEntity entity,
                       UpdateResortBasicInfoRequest request,
                       CountryEntity countryEntity,
                       CityEntity cityEntity) {
        applyCommonFields(entity, request, countryEntity, cityEntity);
    }

    private void applyCommonFields(ResortBasicInfoEntity entity,
                                   ResortBasicInfoRequest request,
                                   CountryEntity countryEntity,
                                   CityEntity cityEntity) {
        entity.setSortOrder(request.getSortOrder());
        entity.setEstd(request.getEstd());
        entity.setCountryEntity(countryEntity);
        entity.setCityEntity(cityEntity);
        entity.setLogoUrl(request.getLogoUrl());
        entity.setLat(request.getLat());
        entity.setLon(request.getLon());
    }

    private Set<ResortBasicInfoLocaleEntity> mapLocales(List<CreateResortBasicInfoLocaleRequest> locales,
                                                        ResortBasicInfoEntity entity,
                                                        Map<Long, LocaleEntity> localeEntityMap) {
        if (locales == null) return new LinkedHashSet<>();
        return locales.stream()
                .map(l -> ResortBasicInfoLocaleMapper.create(l, entity, localeEntityMap.get(l.getLocaleId())))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public ResortBasicInfoDto toDto(ResortBasicInfoEntity entity) {
        List<ResortBasicInfoLocaleDto> locales = entity.getLocaleEntities().stream()
                .map(ResortBasicInfoLocaleMapper::toDto)
                .toList();
        return ResortBasicInfoDto.builder()
                .id(entity.getId())
                .resortId(entity.getResortEntity().getId())
                .code(entity.getCode())
                .sortOrder(entity.getSortOrder())
                .estd(entity.getEstd())
                .countryId(entity.getCountryEntity().getId())
                .cityId(entity.getCityEntity().getId())
                .logoUrl(entity.getLogoUrl())
                .lat(entity.getLat())
                .lon(entity.getLon())
                .locales(locales)
                .build();
    }
}
