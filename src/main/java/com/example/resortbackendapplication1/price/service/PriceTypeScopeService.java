package com.example.resortbackendapplication1.price.service;

import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.price.dto.request.pricetypescope.CreatePriceTypeScopeRequest;
import com.example.resortbackendapplication1.price.dto.request.pricetypescope.PriceTypeScopeFilterRequest;
import com.example.resortbackendapplication1.price.dto.request.pricetypescope.UpdatePriceTypeScopeRequest;
import com.example.resortbackendapplication1.price.dto.response.pricetypescopes.PriceTypeScopeResponse;
import com.example.resortbackendapplication1.price.model.dto.PriceTypeScopeDto;
import com.example.resortbackendapplication1.price.model.entity.PriceTypeScopeEntity;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;

public interface PriceTypeScopeService {

    SuccessResponse create(CreatePriceTypeScopeRequest request,
                           LocaleEntity localeEntity);

    PriceTypeScopeEntity getEntityById(Long id);

    PriceTypeScopeResponse getById(Long id);

    PaginatedResponse<PriceTypeScopeDto> getAll(PriceTypeScopeFilterRequest request);

    SuccessResponse update(PriceTypeScopeEntity entity,
                           UpdatePriceTypeScopeRequest request);

    SuccessResponse delete(PriceTypeScopeEntity entity);
}
