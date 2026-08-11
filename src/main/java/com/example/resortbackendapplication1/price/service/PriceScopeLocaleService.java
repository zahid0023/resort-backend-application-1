package com.example.resortbackendapplication1.price.service;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.price.dto.request.pricescope.locale.CreatePriceScopeLocaleRequest;
import com.example.resortbackendapplication1.price.dto.request.pricescope.locale.UpdatePriceScopeLocaleRequest;
import com.example.resortbackendapplication1.price.model.dto.PriceScopeLocaleDto;
import com.example.resortbackendapplication1.price.model.entity.PriceScopeEntity;
import com.example.resortbackendapplication1.price.model.entity.PriceScopeLocaleEntity;
import com.example.resortbackendapplication1.locale.dto.response.locales.LocaleCountResponse;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;

public interface PriceScopeLocaleService {
    SuccessResponse create(CreatePriceScopeLocaleRequest request,
                           PriceScopeEntity priceScopeEntity,
                           LocaleEntity localeEntity);

    PriceScopeLocaleEntity getEntityById(Long priceScopeId, Long id);

    PaginatedResponse<PriceScopeLocaleDto> getAll(Long priceScopeId, String localeCode, PaginatedRequest paginatedRequest);

    LocaleCountResponse getCount(Long priceScopeId);

    SuccessResponse update(PriceScopeLocaleEntity entity,
                           UpdatePriceScopeLocaleRequest request);

    SuccessResponse delete(PriceScopeLocaleEntity entity);
}
