package com.example.resortbackendapplication1.bedtype.service;

import com.example.resortbackendapplication1.bedtype.dto.request.bedtype.locale.CreateBedTypeLocaleRequest;
import com.example.resortbackendapplication1.bedtype.dto.request.bedtype.locale.UpdateBedTypeLocaleRequest;
import com.example.resortbackendapplication1.bedtype.model.dto.BedTypeLocaleDto;
import com.example.resortbackendapplication1.bedtype.model.entity.BedTypeEntity;
import com.example.resortbackendapplication1.bedtype.model.entity.BedTypeLocaleEntity;
import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;

public interface BedTypeLocaleService {
    SuccessResponse create(CreateBedTypeLocaleRequest request,
                           BedTypeEntity bedTypeEntity,
                           LocaleEntity localeEntity);

    BedTypeLocaleEntity getEntityById(Long bedTypeId, Long id);

    PaginatedResponse<BedTypeLocaleDto> getAll(Long bedTypeId, String localeCode, PaginatedRequest paginatedRequest);

    SuccessResponse update(BedTypeLocaleEntity entity,
                           UpdateBedTypeLocaleRequest request);

    SuccessResponse delete(BedTypeLocaleEntity entity);
}
