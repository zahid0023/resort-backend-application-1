package com.example.resortbackendapplication1.unit.service;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.locale.dto.response.locales.LocaleCountResponse;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.unit.dto.request.unit.locale.CreateUnitLocaleRequest;
import com.example.resortbackendapplication1.unit.dto.request.unit.locale.UpdateUnitLocaleRequest;
import com.example.resortbackendapplication1.unit.model.dto.UnitLocaleDto;
import com.example.resortbackendapplication1.unit.model.entity.UnitEntity;
import com.example.resortbackendapplication1.unit.model.entity.UnitLocaleEntity;

public interface UnitLocaleService {
    SuccessResponse create(CreateUnitLocaleRequest request,
                           UnitEntity unitEntity,
                           LocaleEntity localeEntity);

    UnitLocaleEntity getEntityById(Long unitId, Long id);

    PaginatedResponse<UnitLocaleDto> getAll(Long unitId, String localeCode, PaginatedRequest paginatedRequest);

    LocaleCountResponse getCount(Long unitId);

    SuccessResponse update(UnitLocaleEntity entity,
                           UpdateUnitLocaleRequest request);

    SuccessResponse delete(UnitLocaleEntity entity);
}
