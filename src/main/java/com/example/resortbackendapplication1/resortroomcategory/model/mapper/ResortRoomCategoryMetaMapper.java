package com.example.resortbackendapplication1.resortroomcategory.model.mapper;

import com.example.resortbackendapplication1.resortroomcategory.dto.request.meta.ResortRoomCategoryMetaRequest;
import com.example.resortbackendapplication1.resortroomcategory.model.dto.ResortRoomCategoryMetaDto;
import com.example.resortbackendapplication1.resortroomcategory.model.entity.ResortRoomCategoryEntity;
import com.example.resortbackendapplication1.resortroomcategory.model.entity.ResortRoomCategoryMetaEntity;
import com.example.resortbackendapplication1.unit.model.entity.UnitEntity;
import com.example.resortbackendapplication1.unit.model.mapper.UnitMapper;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ResortRoomCategoryMetaMapper {

    public ResortRoomCategoryMetaEntity create(ResortRoomCategoryMetaRequest request,
                                               ResortRoomCategoryEntity resortRoomCategoryEntity,
                                               UnitEntity roomSizeUnit) {
        ResortRoomCategoryMetaEntity entity = new ResortRoomCategoryMetaEntity();
        entity.setResortRoomCategoryEntity(resortRoomCategoryEntity);
        applyCommonFields(entity, request, roomSizeUnit);
        return entity;
    }

    public void update(ResortRoomCategoryMetaEntity entity,
                       ResortRoomCategoryMetaRequest request,
                       UnitEntity roomSizeUnit) {
        applyCommonFields(entity, request, roomSizeUnit);
    }

    private void applyCommonFields(ResortRoomCategoryMetaEntity entity,
                                   ResortRoomCategoryMetaRequest request,
                                   UnitEntity roomSizeUnit) {
        entity.setMaxAdults(request.getMaxAdults());
        entity.setMaxChildren(request.getMaxChildren());
        entity.setMaxInfants(request.getMaxInfants());
        entity.setMaxOccupancy(request.getMaxOccupancy());
        entity.setRoomSize(request.getRoomSize());
        entity.setRoomSizeUnit(roomSizeUnit);
        entity.setBedroomCount(request.getBedroomCount());
        entity.setBathroomCount(request.getBathroomCount());
        entity.setDefaultCheckInTime(request.getDefaultCheckInTime());
        entity.setDefaultCheckOutTime(request.getDefaultCheckOutTime());
        entity.setIsExtraBedAllowed(request.getIsExtraBedAllowed());
        entity.setMaxExtraBeds(request.getMaxExtraBeds());
        entity.setIsSmokingAllowed(request.getIsSmokingAllowed());
        entity.setIsPetsAllowed(request.getIsPetsAllowed());
        entity.setMinimumStayNights(request.getMinimumStayNights());
        entity.setMaximumStayNights(request.getMaximumStayNights());
    }

    public ResortRoomCategoryMetaDto toDto(ResortRoomCategoryMetaEntity entity) {
        if (entity == null) return null;
        return ResortRoomCategoryMetaDto.builder()
                .id(entity.getId())
                .maxAdults(entity.getMaxAdults())
                .maxChildren(entity.getMaxChildren())
                .maxInfants(entity.getMaxInfants())
                .maxOccupancy(entity.getMaxOccupancy())
                .roomSize(entity.getRoomSize())
                .roomSizeUnit(entity.getRoomSizeUnit() != null ? UnitMapper.toDto(entity.getRoomSizeUnit()) : null)
                .bedroomCount(entity.getBedroomCount())
                .bathroomCount(entity.getBathroomCount())
                .defaultCheckInTime(entity.getDefaultCheckInTime())
                .defaultCheckOutTime(entity.getDefaultCheckOutTime())
                .isExtraBedAllowed(entity.getIsExtraBedAllowed())
                .maxExtraBeds(entity.getMaxExtraBeds())
                .isSmokingAllowed(entity.getIsSmokingAllowed())
                .isPetsAllowed(entity.getIsPetsAllowed())
                .minimumStayNights(entity.getMinimumStayNights())
                .maximumStayNights(entity.getMaximumStayNights())
                .build();
    }
}
