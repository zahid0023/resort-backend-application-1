package com.example.resortbackendapplication1.resortroomcategory.model.mapper;

import com.example.resortbackendapplication1.bedtype.model.entity.BedTypeEntity;
import com.example.resortbackendapplication1.bedtype.model.mapper.BedTypeMapper;
import com.example.resortbackendapplication1.resortroomcategory.dto.request.bed.ResortRoomCategoryBedRequest;
import com.example.resortbackendapplication1.resortroomcategory.dto.request.bed.UpdateResortRoomCategoryBedRequest;
import com.example.resortbackendapplication1.resortroomcategory.model.dto.ResortRoomCategoryBedDto;
import com.example.resortbackendapplication1.resortroomcategory.model.entity.ResortRoomCategoryBedEntity;
import com.example.resortbackendapplication1.resortroomcategory.model.entity.ResortRoomCategoryEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ResortRoomCategoryBedMapper {

    public ResortRoomCategoryBedEntity create(ResortRoomCategoryBedRequest request,
                                              ResortRoomCategoryEntity resortRoomCategoryEntity,
                                              BedTypeEntity bedTypeEntity) {
        ResortRoomCategoryBedEntity entity = new ResortRoomCategoryBedEntity();
        entity.setResortRoomCategoryEntity(resortRoomCategoryEntity);
        entity.setBedTypeEntity(bedTypeEntity);
        entity.setQuantity(request.getQuantity());
        return entity;
    }

    public void update(ResortRoomCategoryBedEntity entity, UpdateResortRoomCategoryBedRequest request) {
        entity.setQuantity(request.getQuantity());
    }

    public ResortRoomCategoryBedDto toDto(ResortRoomCategoryBedEntity entity) {
        return ResortRoomCategoryBedDto.builder()
                .id(entity.getId())
                .bedType(BedTypeMapper.toDto(entity.getBedTypeEntity()))
                .quantity(entity.getQuantity())
                .build();
    }
}
