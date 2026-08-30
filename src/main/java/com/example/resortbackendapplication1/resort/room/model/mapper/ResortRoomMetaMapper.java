package com.example.resortbackendapplication1.resort.room.model.mapper;

import com.example.resortbackendapplication1.resort.room.dto.request.resortroommeta.CreateResortRoomMetaRequest;
import com.example.resortbackendapplication1.resort.room.dto.request.resortroommeta.ResortRoomMetaRequest;
import com.example.resortbackendapplication1.resort.room.dto.request.resortroommeta.UpdateResortRoomMetaRequest;
import com.example.resortbackendapplication1.resort.room.model.dto.ResortRoomMetaDto;
import com.example.resortbackendapplication1.resort.room.model.entity.ResortRoomMetaEntity;
import com.example.resortbackendapplication1.resort.roomcategory.model.dto.ResortRoomCategoryMetaDto;
import com.example.resortbackendapplication1.unit.model.entity.UnitEntity;
import com.example.resortbackendapplication1.unit.model.mapper.UnitMapper;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ResortRoomMetaMapper {

    public ResortRoomMetaEntity create(CreateResortRoomMetaRequest request, UnitEntity roomSizeUnitEntity) {
        ResortRoomMetaEntity entity = new ResortRoomMetaEntity();
        applyCommonFields(entity, request);
        entity.setRoomSizeUnitEntity(roomSizeUnitEntity);
        return entity;
    }

    public void update(ResortRoomMetaEntity entity, UpdateResortRoomMetaRequest request, UnitEntity roomSizeUnitEntity) {
        applyCommonFields(entity, request);
        entity.setRoomSizeUnitEntity(roomSizeUnitEntity);
    }

    private void applyCommonFields(ResortRoomMetaEntity entity, ResortRoomMetaRequest request) {
        entity.setMaxAdults(request.getMaxAdults());
        entity.setMaxChildren(request.getMaxChildren());
        entity.setMaxInfants(request.getMaxInfants());
        entity.setMaxOccupancy(request.getMaxOccupancy());
        entity.setRoomSize(request.getRoomSize());
        entity.setBedroomCount(request.getBedroomCount());
        entity.setBathroomCount(request.getBathroomCount());
        entity.setMinimumStayNights(request.getMinimumStayNights());
        entity.setMaximumStayNights(request.getMaximumStayNights());
    }

    public ResortRoomMetaDto.ResortRoomMetaDtoBuilder toDto(ResortRoomMetaEntity entity) {
        return ResortRoomMetaDto.builder()
                .id(entity.getId())
                .maxAdults(entity.getMaxAdults())
                .maxChildren(entity.getMaxChildren())
                .maxInfants(entity.getMaxInfants())
                .maxOccupancy(entity.getMaxOccupancy())
                .roomSize(entity.getRoomSize())
                .roomSizeUnit(entity.getRoomSizeUnitEntity() != null ? UnitMapper.toDto(entity.getRoomSizeUnitEntity()).build() : null)
                .bedroomCount(entity.getBedroomCount())
                .bathroomCount(entity.getBathroomCount())
                .minimumStayNights(entity.getMinimumStayNights())
                .maximumStayNights(entity.getMaximumStayNights())
                .inherited(false);
    }

    /**
     * Converts a room CATEGORY's meta into the room-meta DTO shape, for the "this room has no own meta
     * override, inherit the category's meta" fallback path in {@code ResortRoomServiceImpl}/
     * {@code ResortRoomMetaServiceImpl}. Lives here (not in a ServiceImpl) because a ServiceImpl must never
     * depend on another entity's DTOs/services directly — see {@code ResortRoomPriceMapper.fromCategoryMain}
     * for the same escape hatch used by prices.
     */
    public ResortRoomMetaDto fromCategory(ResortRoomCategoryMetaDto categoryDto) {
        return ResortRoomMetaDto.builder()
                .id(categoryDto.getId())
                .maxAdults(categoryDto.getMaxAdults())
                .maxChildren(categoryDto.getMaxChildren())
                .maxInfants(categoryDto.getMaxInfants())
                .maxOccupancy(categoryDto.getMaxOccupancy())
                .roomSize(categoryDto.getRoomSize())
                .roomSizeUnit(categoryDto.getRoomSizeUnit())
                .bedroomCount(categoryDto.getBedroomCount())
                .bathroomCount(categoryDto.getBathroomCount())
                .minimumStayNights(categoryDto.getMinimumStayNights())
                .maximumStayNights(categoryDto.getMaximumStayNights())
                .inherited(true)
                .build();
    }
}
