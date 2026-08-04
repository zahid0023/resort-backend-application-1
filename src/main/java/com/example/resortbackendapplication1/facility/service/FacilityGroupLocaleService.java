package com.example.resortbackendapplication1.facility.service;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.facility.dto.request.facilitygroup.locale.CreateFacilityGroupLocaleRequest;
import com.example.resortbackendapplication1.facility.dto.request.facilitygroup.locale.UpdateFacilityGroupLocaleRequest;
import com.example.resortbackendapplication1.facility.model.dto.FacilityGroupLocaleDto;
import com.example.resortbackendapplication1.facility.model.entity.FacilityGroupEntity;
import com.example.resortbackendapplication1.facility.model.entity.FacilityGroupLocaleEntity;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;

public interface FacilityGroupLocaleService {
    SuccessResponse create(CreateFacilityGroupLocaleRequest request,
                           FacilityGroupEntity facilityGroupEntity,
                           LocaleEntity localeEntity);

    FacilityGroupLocaleEntity getEntityById(Long facilityGroupId, Long id);

    PaginatedResponse<FacilityGroupLocaleDto> getAll(Long facilityGroupId, String localeCode, PaginatedRequest paginatedRequest);

    SuccessResponse update(FacilityGroupLocaleEntity entity,
                           UpdateFacilityGroupLocaleRequest request);

    SuccessResponse delete(FacilityGroupLocaleEntity entity);
}
