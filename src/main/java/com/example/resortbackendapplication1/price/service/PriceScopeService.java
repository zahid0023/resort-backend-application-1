package com.example.resortbackendapplication1.price.service;

import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.price.dto.request.pricescope.CreatePriceScopeRequest;
import com.example.resortbackendapplication1.price.dto.request.pricescope.PriceScopeFilterRequest;
import com.example.resortbackendapplication1.price.dto.request.pricescope.UpdatePriceScopeRequest;
import com.example.resortbackendapplication1.price.dto.response.pricescopes.PriceScopeCountResponse;
import com.example.resortbackendapplication1.price.dto.response.pricescopes.PriceScopeResponse;
import com.example.resortbackendapplication1.price.model.dto.PriceScopeDto;
import com.example.resortbackendapplication1.price.model.entity.PriceScopeEntity;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;

import java.util.List;
import java.util.Set;

public interface PriceScopeService {

    SuccessResponse create(CreatePriceScopeRequest request,
                           LocaleEntity localeEntity);

    PriceScopeEntity getEntityById(Long id);

    List<PriceScopeEntity> getAll(Set<Long> ids);

    PriceScopeResponse getById(Long id);

    PaginatedResponse<PriceScopeDto> getAll(PriceScopeFilterRequest request);

    PriceScopeCountResponse getActiveCount();

    SuccessResponse update(PriceScopeEntity entity,
                           UpdatePriceScopeRequest request);

    SuccessResponse delete(PriceScopeEntity entity);
}
