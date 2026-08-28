package com.example.resortbackendapplication1.resort.room.model.mapper;

import com.example.resortbackendapplication1.resort.room.dto.request.resortroombed.CreateResortRoomBedRequest;
import com.example.resortbackendapplication1.resort.room.dto.request.resortroombed.ResortRoomBedRequest;
import com.example.resortbackendapplication1.resort.room.dto.request.resortroombed.UpdateResortRoomBedRequest;
import com.example.resortbackendapplication1.resort.room.model.dto.ResortRoomBedDto;
import com.example.resortbackendapplication1.resort.room.model.entity.ResortRoomBedEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ResortRoomBedMapper {

    public ResortRoomBedEntity create(CreateResortRoomBedRequest request) {
        ResortRoomBedEntity entity = new ResortRoomBedEntity();
        applyCommonFields(entity, request);
        return entity;
    }

    public void update(ResortRoomBedEntity entity, UpdateResortRoomBedRequest request) {
        applyCommonFields(entity, request);
    }

    private void applyCommonFields(ResortRoomBedEntity entity, ResortRoomBedRequest request) {
        entity.setQuantity(request.getQuantity());
        entity.setIsExtraBedAllowed(request.getIsExtraBedAllowed());
        entity.setMaxExtraBeds(request.getMaxExtraBeds());
    }

    public ResortRoomBedDto.ResortRoomBedDtoBuilder toDto(ResortRoomBedEntity entity) {
        return ResortRoomBedDto.builder()
                .id(entity.getId())
                .quantity(entity.getQuantity())
                .isExtraBedAllowed(entity.getIsExtraBedAllowed())
                .maxExtraBeds(entity.getMaxExtraBeds());
    }
}
