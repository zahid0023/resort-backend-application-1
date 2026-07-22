package com.example.resortbackendapplication1.resortfacilityprice.model.mapper;

import com.example.resortbackendapplication1.currency.model.entity.CurrencyEntity;
import com.example.resortbackendapplication1.currency.model.mapper.CurrencyMapper;
import com.example.resortbackendapplication1.price.model.entity.PriceUnitEntity;
import com.example.resortbackendapplication1.price.model.mapper.PriceUnitMapper;
import com.example.resortbackendapplication1.resort.model.entity.ResortFacilityEntity;
import com.example.resortbackendapplication1.resortfacilityprice.dto.request.CreateResortFacilityPriceRequest;
import com.example.resortbackendapplication1.resortfacilityprice.dto.request.ResortFacilityPriceRequest;
import com.example.resortbackendapplication1.resortfacilityprice.dto.request.UpdateResortFacilityPriceRequest;
import com.example.resortbackendapplication1.resortfacilityprice.model.dto.ResortFacilityPriceDto;
import com.example.resortbackendapplication1.resortfacilityprice.model.entity.ResortFacilityPriceEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ResortFacilityPriceMapper {

    public ResortFacilityPriceEntity create(CreateResortFacilityPriceRequest request,
                                            ResortFacilityEntity resortFacilityEntity,
                                            PriceUnitEntity priceUnitEntity,
                                            CurrencyEntity currencyEntity) {
        ResortFacilityPriceEntity entity = new ResortFacilityPriceEntity();
        entity.setResortFacilityEntity(resortFacilityEntity);
        entity.setCurrencyEntity(currencyEntity);
        applyCommonFields(entity, request, priceUnitEntity);
        return entity;
    }

    public void update(ResortFacilityPriceEntity entity,
                       UpdateResortFacilityPriceRequest request,
                       PriceUnitEntity priceUnitEntity) {
        applyCommonFields(entity, request, priceUnitEntity);
    }

    private void applyCommonFields(ResortFacilityPriceEntity entity,
                                   ResortFacilityPriceRequest request,
                                   PriceUnitEntity priceUnitEntity) {
        entity.setPriceUnitEntity(priceUnitEntity);
        entity.setAmount(request.getAmount());
        entity.setNotes(request.getNotes() != null ? request.getNotes() : "");
        entity.setSortOrder(request.getSortOrder());
    }

    public ResortFacilityPriceDto toDto(ResortFacilityPriceEntity entity) {
        return ResortFacilityPriceDto.builder()
                .id(entity.getId())
                .resortFacilityId(entity.getResortFacilityEntity().getId())
                .priceUnit(PriceUnitMapper.toDto(entity.getPriceUnitEntity()))
                .currency(CurrencyMapper.toDto(entity.getCurrencyEntity()))
                .amount(entity.getAmount())
                .notes(entity.getNotes())
                .sortOrder(entity.getSortOrder())
                .build();
    }
}
