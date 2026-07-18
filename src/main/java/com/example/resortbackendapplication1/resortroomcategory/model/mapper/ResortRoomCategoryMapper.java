package com.example.resortbackendapplication1.resortroomcategory.model.mapper;

import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.resort.model.entity.ResortEntity;
import com.example.resortbackendapplication1.resortroomcategory.dto.request.CreateResortRoomCategoryRequest;
import com.example.resortbackendapplication1.resortroomcategory.dto.request.ResortRoomCategoryRequest;
import com.example.resortbackendapplication1.resortroomcategory.dto.request.UpdateResortRoomCategoryRequest;
import com.example.resortbackendapplication1.resortroomcategory.dto.request.locale.CreateResortRoomCategoryLocaleRequest;
import com.example.resortbackendapplication1.resortroomcategory.model.dto.ResortRoomCategoryDto;
import com.example.resortbackendapplication1.resortroomcategory.model.dto.ResortRoomCategoryLocaleDto;
import com.example.resortbackendapplication1.resortroomcategory.model.entity.ResortRoomCategoryEntity;
import com.example.resortbackendapplication1.resortroomcategory.model.entity.ResortRoomCategoryLocaleEntity;
import com.example.resortbackendapplication1.roomcategory.model.entity.RoomCategoryEntity;
import lombok.experimental.UtilityClass;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class ResortRoomCategoryMapper {

    public ResortRoomCategoryEntity create(CreateResortRoomCategoryRequest request,
                                            ResortEntity resortEntity,
                                            RoomCategoryEntity roomCategoryEntity,
                                            Map<Long, LocaleEntity> localeEntityMap) {
        ResortRoomCategoryEntity entity = new ResortRoomCategoryEntity();
        entity.setResortEntity(resortEntity);
        entity.setRoomCategoryEntity(roomCategoryEntity);
        entity.setCode(request.getCode());
        applyCommonFields(entity, request);
        entity.setResortRoomCategoryLocaleEntities(mapLocales(request.getLocales(), entity, localeEntityMap));
        return entity;
    }

    public void update(ResortRoomCategoryEntity entity, UpdateResortRoomCategoryRequest request) {
        applyCommonFields(entity, request);
    }

    private void applyCommonFields(ResortRoomCategoryEntity entity, ResortRoomCategoryRequest request) {
        entity.setSortOrder(request.getSortOrder());
        entity.setMaxAdults(request.getMaxAdults());
        entity.setMaxChildren(request.getMaxChildren());
        entity.setMaxOccupancy(request.getMaxOccupancy());
        entity.setDefaultCheckInTime(request.getDefaultCheckInTime());
        entity.setDefaultCheckOutTime(request.getDefaultCheckOutTime());
        entity.setIsExtraBedAllowed(request.getIsExtraBedAllowed());
        entity.setMaxExtraBeds(request.getMaxExtraBeds());
        entity.setIsSmokingAllowed(request.getIsSmokingAllowed());
        entity.setIsPetsAllowed(request.getIsPetsAllowed());
    }

    private Set<ResortRoomCategoryLocaleEntity> mapLocales(List<CreateResortRoomCategoryLocaleRequest> locales,
                                                            ResortRoomCategoryEntity entity,
                                                            Map<Long, LocaleEntity> localeEntityMap) {
        if (locales == null || locales.isEmpty()) return new LinkedHashSet<>();
        return locales.stream()
                .map(locale -> ResortRoomCategoryLocaleMapper.create(locale, entity, localeEntityMap.get(locale.getLocaleId())))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public ResortRoomCategoryDto toDto(ResortRoomCategoryEntity entity) {
        List<ResortRoomCategoryLocaleDto> locales = entity.getResortRoomCategoryLocaleEntities().stream()
                .map(ResortRoomCategoryLocaleMapper::toDto)
                .toList();

        return ResortRoomCategoryDto.builder()
                .id(entity.getId())
                .resortId(entity.getResortEntity().getId())
                .roomCategoryId(entity.getRoomCategoryEntity().getId())
                .code(entity.getCode())
                .sortOrder(entity.getSortOrder())
                .maxAdults(entity.getMaxAdults())
                .maxChildren(entity.getMaxChildren())
                .maxOccupancy(entity.getMaxOccupancy())
                .defaultCheckInTime(entity.getDefaultCheckInTime())
                .defaultCheckOutTime(entity.getDefaultCheckOutTime())
                .isExtraBedAllowed(entity.getIsExtraBedAllowed())
                .maxExtraBeds(entity.getMaxExtraBeds())
                .isSmokingAllowed(entity.getIsSmokingAllowed())
                .isPetsAllowed(entity.getIsPetsAllowed())
                .locales(locales)
                .build();
    }
}
