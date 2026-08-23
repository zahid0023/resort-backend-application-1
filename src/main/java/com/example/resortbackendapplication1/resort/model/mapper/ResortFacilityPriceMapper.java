package com.example.resortbackendapplication1.resort.model.mapper;

import com.example.resortbackendapplication1.currency.model.entity.CurrencyEntity;
import com.example.resortbackendapplication1.price.model.entity.PriceTypeEntity;
import com.example.resortbackendapplication1.price.model.entity.PriceUnitEntity;
import com.example.resortbackendapplication1.resort.dto.request.resortfacilityprice.CreateResortFacilityPriceRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortfacilityprice.ResortFacilityPriceRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortfacilityprice.UpdateResortFacilityPriceRequest;
import com.example.resortbackendapplication1.resort.model.dto.ResortFacilityPriceDto;
import com.example.resortbackendapplication1.resort.model.entity.ResortFacilityPriceEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ResortFacilityPriceMapper {

    public ResortFacilityPriceEntity create(CreateResortFacilityPriceRequest request,
                                            PriceTypeEntity priceTypeEntity,
                                            PriceUnitEntity priceUnitEntity,
                                            CurrencyEntity currencyEntity) {
        ResortFacilityPriceEntity entity = new ResortFacilityPriceEntity();
        entity.setPriceTypeEntity(priceTypeEntity);
        entity.setPriceUnitEntity(priceUnitEntity);
        entity.setCurrencyEntity(currencyEntity);
        applyCommonFields(entity, request);
        return entity;
    }

    public void update(ResortFacilityPriceEntity entity, UpdateResortFacilityPriceRequest request) {
        applyCommonFields(entity, request);
    }

    private void applyCommonFields(ResortFacilityPriceEntity entity, ResortFacilityPriceRequest request) {
        entity.setAmount(request.getAmount());
        entity.setNote(request.getNote() != null ? request.getNote() : "");
    }

    public ResortFacilityPriceDto.ResortFacilityPriceDtoBuilder toDto(ResortFacilityPriceEntity entity) {
        return ResortFacilityPriceDto.builder()
                .id(entity.getId())
                .amount(entity.getAmount())
                .note(entity.getNote());
    }
}
