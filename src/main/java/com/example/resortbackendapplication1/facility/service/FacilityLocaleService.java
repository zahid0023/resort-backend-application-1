package com.example.resortbackendapplication1.facility.service;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.facility.dto.request.facility.locale.CreateFacilityLocaleRequest;
import com.example.resortbackendapplication1.facility.dto.request.facility.locale.UpdateFacilityLocaleRequest;
import com.example.resortbackendapplication1.facility.model.dto.FacilityLocaleDto;
import com.example.resortbackendapplication1.facility.model.entity.FacilityEntity;
import com.example.resortbackendapplication1.facility.model.entity.FacilityLocaleEntity;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;

public interface FacilityLocaleService {
    SuccessResponse create(CreateFacilityLocaleRequest request,
                           FacilityEntity facilityEntity,
                           LocaleEntity localeEntity);

    FacilityLocaleEntity getEntityById(Long facilityId, Long id);

    PaginatedResponse<FacilityLocaleDto> getAll(Long facilityId, String localeCode, PaginatedRequest paginatedRequest);

    SuccessResponse update(FacilityLocaleEntity entity,
                           UpdateFacilityLocaleRequest request);

    SuccessResponse delete(FacilityLocaleEntity entity);
}
