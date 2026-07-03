package com.example.resortbackendapplication1.price.service;

import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.price.dto.request.pricetype.pricetypelocale.CreatePriceTypeLocaleRequest;
import com.example.resortbackendapplication1.price.dto.request.pricetype.pricetypelocale.UpdatePriceTypeLocaleRequest;
import com.example.resortbackendapplication1.price.model.entity.PriceTypeEntity;
import com.example.resortbackendapplication1.price.model.entity.PriceTypeLocaleEntity;

public interface PriceTypeLocaleService {

    SuccessResponse create(PriceTypeEntity priceTypeEntity,
                           LocaleEntity localeEntity,
                           CreatePriceTypeLocaleRequest request);

    PriceTypeLocaleEntity getEntityById(Long priceTypeId, Long id);

    SuccessResponse update(PriceTypeLocaleEntity entity,
                           UpdatePriceTypeLocaleRequest request);

    SuccessResponse delete(PriceTypeLocaleEntity entity);
}
