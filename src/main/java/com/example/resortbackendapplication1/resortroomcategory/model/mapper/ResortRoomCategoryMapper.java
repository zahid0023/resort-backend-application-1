package com.example.resortbackendapplication1.resortroomcategory.model.mapper;

import com.example.resortbackendapplication1.bedtype.model.entity.BedTypeEntity;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.resort.model.entity.ResortEntity;
import com.example.resortbackendapplication1.roomcategory.model.mapper.RoomCategoryMapper;
import com.example.resortbackendapplication1.unit.model.entity.UnitEntity;
import com.example.resortbackendapplication1.resortroomcategory.dto.request.CreateResortRoomCategoryRequest;
import com.example.resortbackendapplication1.resortroomcategory.dto.request.ResortRoomCategoryRequest;
import com.example.resortbackendapplication1.resortroomcategory.dto.request.UpdateResortRoomCategoryRequest;
import com.example.resortbackendapplication1.resortroomcategory.dto.request.bed.ResortRoomCategoryBedRequest;
import com.example.resortbackendapplication1.resortroomcategory.dto.request.locale.CreateResortRoomCategoryLocaleRequest;
import com.example.resortbackendapplication1.resortroomcategory.model.dto.ResortRoomCategoryBedDto;
import com.example.resortbackendapplication1.resortroomcategory.model.dto.ResortRoomCategoryDto;
import com.example.resortbackendapplication1.resortroomcategory.model.dto.ResortRoomCategoryLocaleDto;
import com.example.resortbackendapplication1.resortroomcategory.model.entity.ResortRoomCategoryBedEntity;
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
                                           Map<Long, LocaleEntity> localeEntityMap,
                                           Map<Long, BedTypeEntity> bedTypeEntityMap,
                                           UnitEntity roomSizeUnit) {
        ResortRoomCategoryEntity entity = new ResortRoomCategoryEntity();
        entity.setResortEntity(resortEntity);
        entity.setRoomCategoryEntity(roomCategoryEntity);
        entity.setCode(request.getCode());
        applyCommonFields(entity, request);
        entity.setMeta(ResortRoomCategoryMetaMapper.create(request.getMeta(), entity, roomSizeUnit));
        entity.setBeds(mapBeds(request.getBeds(), entity, bedTypeEntityMap));
        entity.setResortRoomCategoryLocaleEntities(mapLocales(request.getLocales(), entity, localeEntityMap));
        return entity;
    }

    public void update(ResortRoomCategoryEntity entity, UpdateResortRoomCategoryRequest request) {
        applyCommonFields(entity, request);
    }

    private void applyCommonFields(ResortRoomCategoryEntity entity, ResortRoomCategoryRequest request) {
        entity.setSortOrder(request.getSortOrder());
    }

    private Set<ResortRoomCategoryBedEntity> mapBeds(List<ResortRoomCategoryBedRequest> beds,
                                                     ResortRoomCategoryEntity entity,
                                                     Map<Long, BedTypeEntity> bedTypeEntityMap) {
        if (beds == null || beds.isEmpty()) return new LinkedHashSet<>();
        return beds.stream()
                .map(bed -> ResortRoomCategoryBedMapper.create(bed, entity, bedTypeEntityMap.get(bed.getBedTypeId())))
                .collect(Collectors.toCollection(LinkedHashSet::new));
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

        List<ResortRoomCategoryBedDto> beds = entity.getBeds().stream()
                .map(ResortRoomCategoryBedMapper::toDto)
                .toList();

        return ResortRoomCategoryDto.builder()
                .id(entity.getId())
                .resortId(entity.getResortEntity().getId())
                .roomCategory(RoomCategoryMapper.toDto(entity.getRoomCategoryEntity()))
                .code(entity.getCode())
                .sortOrder(entity.getSortOrder())
                .meta(ResortRoomCategoryMetaMapper.toDto(entity.getMeta()))
                .beds(beds)
                .locales(locales)
                .build();
    }
}
