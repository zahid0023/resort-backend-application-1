package com.example.resortbackendapplication1.resort.roomcategory.model.mapper;

import com.example.resortbackendapplication1.resort.roomcategory.dto.request.resortroomcategorybed.CreateResortRoomCategoryBedRequest;
import com.example.resortbackendapplication1.resort.roomcategory.dto.request.resortroomcategorybed.ResortRoomCategoryBedRequest;
import com.example.resortbackendapplication1.resort.roomcategory.dto.request.resortroomcategorybed.UpdateResortRoomCategoryBedRequest;
import com.example.resortbackendapplication1.resort.roomcategory.model.dto.ResortRoomCategoryBedDto;
import com.example.resortbackendapplication1.resort.roomcategory.model.entity.ResortRoomCategoryBedEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ResortRoomCategoryBedMapper {

    public ResortRoomCategoryBedEntity create(CreateResortRoomCategoryBedRequest request) {
        ResortRoomCategoryBedEntity entity = new ResortRoomCategoryBedEntity();
        applyCommonFields(entity, request);
        return entity;
    }

    public void update(ResortRoomCategoryBedEntity entity, UpdateResortRoomCategoryBedRequest request) {
        applyCommonFields(entity, request);
    }

    private void applyCommonFields(ResortRoomCategoryBedEntity entity, ResortRoomCategoryBedRequest request) {
        entity.setQuantity(request.getQuantity());
        entity.setIsExtraBedAllowed(request.getIsExtraBedAllowed());
        entity.setMaxExtraBeds(request.getMaxExtraBeds());
    }

    public ResortRoomCategoryBedDto.ResortRoomCategoryBedDtoBuilder toDto(ResortRoomCategoryBedEntity entity) {
        return ResortRoomCategoryBedDto.builder()
                .id(entity.getId())
                .quantity(entity.getQuantity())
                .isExtraBedAllowed(entity.getIsExtraBedAllowed())
                .maxExtraBeds(entity.getMaxExtraBeds());
    }
}
