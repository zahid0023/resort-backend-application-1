package com.example.resortbackendapplication1.price.service;

import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.price.dto.request.pricetype.CreatePriceTypeRequest;
import com.example.resortbackendapplication1.price.dto.request.pricetype.PriceTypeFilterRequest;
import com.example.resortbackendapplication1.price.dto.request.pricetype.UpdatePriceTypeRequest;
import com.example.resortbackendapplication1.price.dto.response.pricetypes.PriceTypeResponse;
import com.example.resortbackendapplication1.price.model.dto.PriceTypeDto;
import com.example.resortbackendapplication1.price.model.entity.PriceTypeEntity;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface PriceTypeService {

    SuccessResponse create(CreatePriceTypeRequest request,
                           Map<Long, LocaleEntity> localeEntityMap);

    PriceTypeEntity getEntityById(Long id);

    PriceTypeResponse getById(Long id);

    PaginatedResponse<PriceTypeDto> getAll(PriceTypeFilterRequest request);

    SuccessResponse update(PriceTypeEntity entity,
                           UpdatePriceTypeRequest request);

    SuccessResponse delete(Long id);

    List<PriceTypeEntity> getAll(Set<Long> ids);
}
