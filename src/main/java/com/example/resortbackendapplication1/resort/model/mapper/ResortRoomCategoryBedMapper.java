package com.example.resortbackendapplication1.resort.model.mapper;

import com.example.resortbackendapplication1.resort.dto.request.resortroomcategorybed.CreateResortRoomCategoryBedRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortroomcategorybed.ResortRoomCategoryBedRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortroomcategorybed.UpdateResortRoomCategoryBedRequest;
import com.example.resortbackendapplication1.resort.model.dto.ResortRoomCategoryBedDto;
import com.example.resortbackendapplication1.resort.model.entity.ResortRoomCategoryBedEntity;
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
