package com.example.resortbackendapplication1.price.service;

import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.price.dto.request.pricetype.CreatePriceTypeRequest;
import com.example.resortbackendapplication1.price.dto.request.pricetype.PriceTypeFilterRequest;
import com.example.resortbackendapplication1.price.dto.request.pricetype.UpdatePriceTypeRequest;
import com.example.resortbackendapplication1.price.dto.response.pricetypes.PriceTypeResponse;
import com.example.resortbackendapplication1.price.model.dto.PriceTypeDto;
import com.example.resortbackendapplication1.price.model.entity.PriceTypeEntity;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;

public interface PriceTypeService {

    SuccessResponse create(CreatePriceTypeRequest request,
                           LocaleEntity localeEntity);

    PriceTypeEntity getEntityById(Long id);

    PriceTypeResponse getById(Long id);

    PaginatedResponse<PriceTypeDto> getAll(PriceTypeFilterRequest request);

    SuccessResponse update(PriceTypeEntity entity,
                           UpdatePriceTypeRequest request);

    SuccessResponse delete(PriceTypeEntity entity);
}
