package com.example.resortbackendapplication1.price.service;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.price.dto.request.pricetype.locale.CreatePriceTypeLocaleRequest;
import com.example.resortbackendapplication1.price.dto.request.pricetype.locale.UpdatePriceTypeLocaleRequest;
import com.example.resortbackendapplication1.price.model.dto.PriceTypeLocaleDto;
import com.example.resortbackendapplication1.price.model.entity.PriceTypeEntity;
import com.example.resortbackendapplication1.price.model.entity.PriceTypeLocaleEntity;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;

public interface PriceTypeLocaleService {
    SuccessResponse create(CreatePriceTypeLocaleRequest request,
                           PriceTypeEntity priceTypeEntity,
                           LocaleEntity localeEntity);

    PriceTypeLocaleEntity getEntityById(Long priceTypeId, Long id);

    PaginatedResponse<PriceTypeLocaleDto> getAll(Long priceTypeId, String localeCode, PaginatedRequest paginatedRequest);

    SuccessResponse update(PriceTypeLocaleEntity entity,
                           UpdatePriceTypeLocaleRequest request);

    SuccessResponse delete(PriceTypeLocaleEntity entity);
}
