package com.example.resortbackendapplication1.resort.address.model.mapper;

import com.example.resortbackendapplication1.locale.model.mapper.LocaleMapper;
import com.example.resortbackendapplication1.resort.address.dto.request.resortaddress.locale.ResortAddressLocaleRequest;
import com.example.resortbackendapplication1.resort.address.dto.request.resortaddress.locale.UpdateResortAddressLocaleRequest;
import com.example.resortbackendapplication1.resort.address.model.dto.ResortAddressLocaleDto;
import com.example.resortbackendapplication1.resort.address.model.entity.ResortAddressLocaleEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ResortAddressLocaleMapper {

    public ResortAddressLocaleEntity create(ResortAddressLocaleRequest request) {
        ResortAddressLocaleEntity entity = new ResortAddressLocaleEntity();
        applyCommonFields(entity, request);
        return entity;
    }

    public void update(ResortAddressLocaleEntity entity, UpdateResortAddressLocaleRequest request) {
        applyCommonFields(entity, request);
    }

    private void applyCommonFields(ResortAddressLocaleEntity entity, ResortAddressLocaleRequest request) {
        entity.setAddress(request.getAddress());
        entity.setSortOrder(request.getSortOrder());
    }

    public ResortAddressLocaleDto toDto(ResortAddressLocaleEntity entity) {
        return ResortAddressLocaleDto.builder()
                .id(entity.getId())
                .locale(LocaleMapper.toDto(entity.getLocaleEntity()))
                .address(entity.getAddress())
                .sortOrder(entity.getSortOrder())
                .build();
    }
}
