package com.example.resortbackendapplication1.price.service;

import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.price.dto.request.priceunit.CreatePriceUnitRequest;
import com.example.resortbackendapplication1.price.dto.request.priceunit.PriceUnitFilterRequest;
import com.example.resortbackendapplication1.price.dto.request.priceunit.UpdatePriceUnitRequest;
import com.example.resortbackendapplication1.price.dto.response.priceunits.PriceUnitResponse;
import com.example.resortbackendapplication1.price.model.dto.PriceUnitDto;
import com.example.resortbackendapplication1.price.model.entity.PriceUnitEntity;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface PriceUnitService {

    SuccessResponse create(CreatePriceUnitRequest request,
                           Map<Long, LocaleEntity> localeEntityMap);

    PriceUnitEntity getEntityById(Long id);

    PriceUnitResponse getById(Long id);

    PaginatedResponse<PriceUnitDto> getAll(PriceUnitFilterRequest request);

    SuccessResponse update(PriceUnitEntity entity,
                           UpdatePriceUnitRequest request);

    SuccessResponse delete(Long id);

    List<PriceUnitEntity> getAll(Set<Long> ids);
}
