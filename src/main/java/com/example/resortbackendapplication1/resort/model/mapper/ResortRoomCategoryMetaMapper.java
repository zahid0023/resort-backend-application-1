package com.example.resortbackendapplication1.resort.model.mapper;

import com.example.resortbackendapplication1.resort.dto.request.resortroomcategorymeta.CreateResortRoomCategoryMetaRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortroomcategorymeta.ResortRoomCategoryMetaRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortroomcategorymeta.UpdateResortRoomCategoryMetaRequest;
import com.example.resortbackendapplication1.resort.model.dto.ResortRoomCategoryMetaDto;
import com.example.resortbackendapplication1.resort.model.entity.ResortRoomCategoryMetaEntity;
import com.example.resortbackendapplication1.unit.model.entity.UnitEntity;
import com.example.resortbackendapplication1.unit.model.mapper.UnitMapper;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ResortRoomCategoryMetaMapper {

    public ResortRoomCategoryMetaEntity create(CreateResortRoomCategoryMetaRequest request, UnitEntity roomSizeUnitEntity) {
        ResortRoomCategoryMetaEntity entity = new ResortRoomCategoryMetaEntity();
        applyCommonFields(entity, request);
        entity.setRoomSizeUnitEntity(roomSizeUnitEntity);
        return entity;
    }

    public void update(ResortRoomCategoryMetaEntity entity, UpdateResortRoomCategoryMetaRequest request, UnitEntity roomSizeUnitEntity) {
        applyCommonFields(entity, request);
        entity.setRoomSizeUnitEntity(roomSizeUnitEntity);
    }

    private void applyCommonFields(ResortRoomCategoryMetaEntity entity, ResortRoomCategoryMetaRequest request) {
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

    public ResortRoomCategoryMetaDto.ResortRoomCategoryMetaDtoBuilder toDto(ResortRoomCategoryMetaEntity entity) {
        return ResortRoomCategoryMetaDto.builder()
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
                .maximumStayNights(entity.getMaximumStayNights());
    }
}
