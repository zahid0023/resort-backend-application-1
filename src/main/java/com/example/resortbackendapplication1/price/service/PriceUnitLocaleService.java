package com.example.resortbackendapplication1.price.service;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.price.dto.request.priceunit.locale.CreatePriceUnitLocaleRequest;
import com.example.resortbackendapplication1.price.dto.request.priceunit.locale.UpdatePriceUnitLocaleRequest;
import com.example.resortbackendapplication1.price.model.dto.PriceUnitLocaleDto;
import com.example.resortbackendapplication1.price.model.entity.PriceUnitEntity;
import com.example.resortbackendapplication1.price.model.entity.PriceUnitLocaleEntity;
import com.example.resortbackendapplication1.locale.dto.response.locales.LocaleCountResponse;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;

public interface PriceUnitLocaleService {
    SuccessResponse create(CreatePriceUnitLocaleRequest request,
                           PriceUnitEntity priceUnitEntity,
                           LocaleEntity localeEntity);

    PriceUnitLocaleEntity getEntityById(Long priceUnitId, Long id);

    PaginatedResponse<PriceUnitLocaleDto> getAll(Long priceUnitId, String localeCode, PaginatedRequest paginatedRequest);

    LocaleCountResponse getCount(Long priceUnitId);

    SuccessResponse update(PriceUnitLocaleEntity entity,
                           UpdatePriceUnitLocaleRequest request);

    SuccessResponse delete(PriceUnitLocaleEntity entity);
}
