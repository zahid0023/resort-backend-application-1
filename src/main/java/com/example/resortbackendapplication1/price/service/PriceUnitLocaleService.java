package com.example.resortbackendapplication1.price.service;

import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.price.dto.request.priceunit.priceunitlocale.CreatePriceUnitLocaleRequest;
import com.example.resortbackendapplication1.price.dto.request.priceunit.priceunitlocale.UpdatePriceUnitLocaleRequest;
import com.example.resortbackendapplication1.price.model.entity.PriceUnitEntity;
import com.example.resortbackendapplication1.price.model.entity.PriceUnitLocaleEntity;

public interface PriceUnitLocaleService {

    SuccessResponse create(PriceUnitEntity priceUnitEntity,
                           LocaleEntity localeEntity,
                           CreatePriceUnitLocaleRequest request);

    PriceUnitLocaleEntity getEntityById(Long priceUnitId, Long id);

    SuccessResponse update(PriceUnitLocaleEntity entity,
                           UpdatePriceUnitLocaleRequest request);

    SuccessResponse delete(PriceUnitLocaleEntity entity);
}
