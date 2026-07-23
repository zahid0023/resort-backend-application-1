package com.example.resortbackendapplication1.resort.model.mapper;

import com.example.resortbackendapplication1.facility.model.entity.FacilityEntity;
import com.example.resortbackendapplication1.facility.model.mapper.FacilityMapper;
import com.example.resortbackendapplication1.facilitypricetype.model.entity.FacilityPriceTypeEntity;
import com.example.resortbackendapplication1.facilitypricetype.model.mapper.FacilityPriceTypeMapper;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.resort.dto.request.resortfacility.CreateResortFacilityRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortfacility.ResortFacilityRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortfacility.UpdateResortFacilityRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortfacility.resortfacilitylocale.CreateResortFacilityLocaleRequest;
import com.example.resortbackendapplication1.resort.model.dto.ResortFacilityDto;
import com.example.resortbackendapplication1.resort.model.dto.ResortFacilityLocaleDto;
import com.example.resortbackendapplication1.resort.model.entity.ResortEntity;
import com.example.resortbackendapplication1.resort.model.entity.ResortFacilityEntity;
import com.example.resortbackendapplication1.resort.model.entity.ResortFacilityGroupEntity;
import com.example.resortbackendapplication1.resort.model.entity.ResortFacilityLocaleEntity;
import com.example.resortbackendapplication1.resortfacilityprice.model.dto.ResortFacilityPriceDto;
import com.example.resortbackendapplication1.resortfacilityprice.model.mapper.ResortFacilityPriceMapper;
import lombok.experimental.UtilityClass;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class ResortFacilityMapper {

    public ResortFacilityEntity create(CreateResortFacilityRequest request,
                                       ResortEntity resortEntity,
                                       ResortFacilityGroupEntity resortFacilityGroupEntity,
                                       FacilityEntity facilityEntity,
                                       FacilityPriceTypeEntity facilityPriceTypeEntity,
                                       Map<Long, LocaleEntity> localeEntityMap) {
        ResortFacilityEntity entity = new ResortFacilityEntity();
        entity.setResortEntity(resortEntity);
        entity.setResortFacilityGroupEntity(resortFacilityGroupEntity);
        entity.setFacilityEntity(facilityEntity);
        entity.setFacilityPriceTypeEntity(facilityPriceTypeEntity);
        applyCommonFields(entity, request);
        entity.setResortFacilityLocaleEntities(mapLocales(request.getLocales(), entity, localeEntityMap));
        return entity;
    }

    public void update(ResortFacilityEntity entity,
                       UpdateResortFacilityRequest request,
                       FacilityEntity facilityEntity,
                       FacilityPriceTypeEntity facilityPriceTypeEntity) {
        entity.setFacilityEntity(facilityEntity);
        entity.setFacilityPriceTypeEntity(facilityPriceTypeEntity);
        applyCommonFields(entity, request);
    }

    private void applyCommonFields(ResortFacilityEntity entity, ResortFacilityRequest request) {
        entity.setSortOrder(request.getSortOrder());
        entity.setIconType(request.getIconType());
        entity.setIconValue(request.getIconValue());
        entity.setIconMeta(request.getIconMeta());
        entity.setIsHighlighted(request.getIsHighlighted() != null && request.getIsHighlighted());
    }

    private Set<ResortFacilityLocaleEntity> mapLocales(List<CreateResortFacilityLocaleRequest> locales,
                                                        ResortFacilityEntity entity,
                                                        Map<Long, LocaleEntity> localeEntityMap) {
        if (locales == null || locales.isEmpty()) return new LinkedHashSet<>();
        return locales.stream()
                .map(locale -> ResortFacilityLocaleMapper.create(locale, entity, localeEntityMap.get(locale.getLocaleId())))
                .collect(Collectors.toSet());
    }

    private static final String PAID_CODE = "PAID";

    public ResortFacilityDto toDto(ResortFacilityEntity entity) {
        List<ResortFacilityLocaleDto> locales = entity.getResortFacilityLocaleEntities().stream()
                .map(ResortFacilityLocaleMapper::toDto)
                .toList();

        boolean isPaid = entity.getFacilityPriceTypeEntity() != null
                && PAID_CODE.equals(entity.getFacilityPriceTypeEntity().getCode());

        List<ResortFacilityPriceDto> prices = isPaid
                ? entity.getResortFacilityPriceEntities().stream()
                        .filter(p -> Boolean.TRUE.equals(p.getIsActive()) && Boolean.FALSE.equals(p.getIsDeleted()))
                        .map(ResortFacilityPriceMapper::toDto)
                        .toList()
                : null;

        return ResortFacilityDto.builder()
                .id(entity.getId())
                .resortId(entity.getResortEntity().getId())
                .resortFacilityGroup(ResortFacilityGroupMapper.toDto(entity.getResortFacilityGroupEntity()))
                .platformFacility(entity.getFacilityEntity() != null ? FacilityMapper.toDto(entity.getFacilityEntity()) : null)
                .facilityPriceType(entity.getFacilityPriceTypeEntity() != null ? FacilityPriceTypeMapper.toDto(entity.getFacilityPriceTypeEntity()) : null)
                .sortOrder(entity.getSortOrder())
                .isHighlighted(entity.getIsHighlighted())
                .iconType(entity.getIconType())
                .iconValue(entity.getIconValue())
                .iconMeta(entity.getIconMeta())
                .locales(locales)
                .prices(prices)
                .build();
    }
}
