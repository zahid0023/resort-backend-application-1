package com.example.resortbackendapplication1.resort.address.model.mapper;

import com.example.resortbackendapplication1.commons.context.LocaleContext;
import com.example.resortbackendapplication1.resort.address.dto.request.resortaddress.CreateResortAddressRequest;
import com.example.resortbackendapplication1.resort.address.dto.request.resortaddress.ResortAddressRequest;
import com.example.resortbackendapplication1.resort.address.dto.request.resortaddress.UpdateResortAddressRequest;
import com.example.resortbackendapplication1.resort.address.model.dto.ResortAddressDto;
import com.example.resortbackendapplication1.resort.address.model.dto.ResortAddressLocaleDto;
import com.example.resortbackendapplication1.resort.address.model.entity.ResortAddressEntity;
import com.example.resortbackendapplication1.resort.address.model.entity.ResortAddressLocaleEntity;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class ResortAddressMapper {

    public ResortAddressEntity create(CreateResortAddressRequest request) {
        ResortAddressEntity entity = new ResortAddressEntity();
        applyCommonFields(entity, request);
        return entity;
    }

    public void update(ResortAddressEntity entity, UpdateResortAddressRequest request) {
        applyCommonFields(entity, request);
    }

    private void applyCommonFields(ResortAddressEntity entity, ResortAddressRequest request) {
        entity.setPostalCode(request.getPostalCode());
        entity.setLat(request.getLat());
        entity.setLon(request.getLon());
    }

    public ResortAddressDto.ResortAddressDtoBuilder toDto(ResortAddressEntity entity) {
        return ResortAddressDto.builder()
                .id(entity.getId())
                .postalCode(entity.getPostalCode())
                .lat(entity.getLat())
                .lon(entity.getLon())
                .locale(singleLocale(entity));
    }

    private ResortAddressLocaleDto singleLocale(ResortAddressEntity entity) {
        ResortAddressLocaleEntity matched = matchLocale(entity, LocaleContext.getLocaleId());
        return matched == null ? null : ResortAddressLocaleMapper.toDto(matched);
    }

    private List<ResortAddressLocaleEntity> activeLocales(ResortAddressEntity entity) {
        return entity.getResortAddressLocaleEntities().stream()
                .filter(localeEntity -> Boolean.TRUE.equals(localeEntity.getIsActive())
                        && Boolean.FALSE.equals(localeEntity.getIsDeleted()))
                .toList();
    }

    private ResortAddressLocaleEntity matchLocale(ResortAddressEntity entity, Long localeId) {
        List<ResortAddressLocaleEntity> activeLocales = activeLocales(entity);
        return activeLocales.stream()
                .filter(localeEntity -> localeEntity.getLocaleEntity().getId().equals(localeId))
                .findFirst()
                .orElseGet(() -> activeLocales.stream()
                        .filter(localeEntity -> "en".equals(localeEntity.getLocaleEntity().getCode()))
                        .findFirst()
                        .orElse(null));
    }
}
