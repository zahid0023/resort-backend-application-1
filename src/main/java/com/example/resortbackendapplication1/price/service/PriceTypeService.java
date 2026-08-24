package com.example.resortbackendapplication1.price.service;

import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.price.dto.request.pricetype.CreatePriceTypeRequest;
import com.example.resortbackendapplication1.price.dto.request.pricetype.PriceTypeFilterRequest;
import com.example.resortbackendapplication1.price.dto.request.pricetype.UpdatePriceTypeRequest;
import com.example.resortbackendapplication1.price.dto.response.pricetypes.PriceTypeResponse;
import com.example.resortbackendapplication1.price.model.dto.PriceTypeDto;
import com.example.resortbackendapplication1.price.model.entity.PriceTypeEntity;
import com.example.resortbackendapplication1.price.model.entity.PriceScopeEntity;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;

import java.util.List;

public interface PriceTypeService {

    SuccessResponse create(CreatePriceTypeRequest request,
                           List<PriceScopeEntity> priceScopeEntities,
                           LocaleEntity localeEntity);

    PriceTypeEntity getEntityById(Long id);

    PriceTypeEntity getEntityByCode(String code);

    PriceTypeResponse getById(Long id);

    PaginatedResponse<PriceTypeDto> getAll(PriceTypeFilterRequest request);

    SuccessResponse update(PriceTypeEntity entity,
                           UpdatePriceTypeRequest request);

    SuccessResponse delete(PriceTypeEntity entity);
}
