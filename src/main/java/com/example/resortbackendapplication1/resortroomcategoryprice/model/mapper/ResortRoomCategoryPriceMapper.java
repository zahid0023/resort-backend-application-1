package com.example.resortbackendapplication1.resortroomcategoryprice.model.mapper;

import com.example.resortbackendapplication1.price.model.entity.PriceTypeEntity;
import com.example.resortbackendapplication1.price.model.entity.PriceUnitEntity;
import com.example.resortbackendapplication1.resortroomcategory.model.entity.ResortRoomCategoryEntity;
import com.example.resortbackendapplication1.resortroomcategoryprice.dto.request.CreateResortRoomCategoryPriceRequest;
import com.example.resortbackendapplication1.resortroomcategoryprice.dto.request.ResortRoomCategoryPriceRequest;
import com.example.resortbackendapplication1.resortroomcategoryprice.dto.request.UpdateResortRoomCategoryPriceRequest;
import com.example.resortbackendapplication1.resortroomcategoryprice.model.dto.ResortRoomCategoryPriceDto;
import com.example.resortbackendapplication1.resortroomcategoryprice.model.entity.ResortRoomCategoryPriceEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ResortRoomCategoryPriceMapper {

    public ResortRoomCategoryPriceEntity create(CreateResortRoomCategoryPriceRequest request,
                                                 ResortRoomCategoryEntity resortRoomCategoryEntity,
                                                 PriceTypeEntity priceTypeEntity,
                                                 PriceUnitEntity priceUnitEntity) {
        ResortRoomCategoryPriceEntity entity = new ResortRoomCategoryPriceEntity();
        entity.setResortRoomCategoryEntity(resortRoomCategoryEntity);
        applyCommonFields(entity, request, priceTypeEntity, priceUnitEntity);
        return entity;
    }

    public void update(ResortRoomCategoryPriceEntity entity,
                       UpdateResortRoomCategoryPriceRequest request,
                       PriceTypeEntity priceTypeEntity,
                       PriceUnitEntity priceUnitEntity) {
        applyCommonFields(entity, request, priceTypeEntity, priceUnitEntity);
    }

    private void applyCommonFields(ResortRoomCategoryPriceEntity entity,
                                   ResortRoomCategoryPriceRequest request,
                                   PriceTypeEntity priceTypeEntity,
                                   PriceUnitEntity priceUnitEntity) {
        entity.setPriceTypeEntity(priceTypeEntity);
        entity.setPriceUnitEntity(priceUnitEntity);
        entity.setAmount(request.getAmount());
        entity.setPriority(request.getPriority());
        entity.setValidFrom(request.getValidFrom());
        entity.setValidTo(request.getValidTo());
        entity.setMonday(request.getMonday());
        entity.setTuesday(request.getTuesday());
        entity.setWednesday(request.getWednesday());
        entity.setThursday(request.getThursday());
        entity.setFriday(request.getFriday());
        entity.setSaturday(request.getSaturday());
        entity.setSunday(request.getSunday());
    }

    public ResortRoomCategoryPriceDto toDto(ResortRoomCategoryPriceEntity entity) {
        return ResortRoomCategoryPriceDto.builder()
                .id(entity.getId())
                .resortRoomCategoryId(entity.getResortRoomCategoryEntity().getId())
                .priceTypeId(entity.getPriceTypeEntity().getId())
                .priceUnitId(entity.getPriceUnitEntity().getId())
                .amount(entity.getAmount())
                .priority(entity.getPriority())
                .validFrom(entity.getValidFrom())
                .validTo(entity.getValidTo())
                .monday(entity.getMonday())
                .tuesday(entity.getTuesday())
                .wednesday(entity.getWednesday())
                .thursday(entity.getThursday())
                .friday(entity.getFriday())
                .saturday(entity.getSaturday())
                .sunday(entity.getSunday())
                .build();
    }
}
