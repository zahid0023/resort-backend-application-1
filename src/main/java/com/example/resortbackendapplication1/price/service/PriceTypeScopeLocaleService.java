package com.example.resortbackendapplication1.price.service;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.price.dto.request.pricetypescope.locale.CreatePriceTypeScopeLocaleRequest;
import com.example.resortbackendapplication1.price.dto.request.pricetypescope.locale.UpdatePriceTypeScopeLocaleRequest;
import com.example.resortbackendapplication1.price.model.dto.PriceTypeScopeLocaleDto;
import com.example.resortbackendapplication1.price.model.entity.PriceTypeScopeEntity;
import com.example.resortbackendapplication1.price.model.entity.PriceTypeScopeLocaleEntity;
import com.example.resortbackendapplication1.locale.dto.response.locales.LocaleCountResponse;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;

public interface PriceTypeScopeLocaleService {
    SuccessResponse create(CreatePriceTypeScopeLocaleRequest request,
                           PriceTypeScopeEntity priceTypeScopeEntity,
                           LocaleEntity localeEntity);

    PriceTypeScopeLocaleEntity getEntityById(Long priceTypeScopeId, Long id);

    PaginatedResponse<PriceTypeScopeLocaleDto> getAll(Long priceTypeScopeId, String localeCode, PaginatedRequest paginatedRequest);

    LocaleCountResponse getCount(Long priceTypeScopeId);

    SuccessResponse update(PriceTypeScopeLocaleEntity entity,
                           UpdatePriceTypeScopeLocaleRequest request);

    SuccessResponse delete(PriceTypeScopeLocaleEntity entity);
}
