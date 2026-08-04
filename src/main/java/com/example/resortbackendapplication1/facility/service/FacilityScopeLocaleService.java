package com.example.resortbackendapplication1.facility.service;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.facility.dto.request.facilityscope.locale.CreateFacilityScopeLocaleRequest;
import com.example.resortbackendapplication1.facility.dto.request.facilityscope.locale.UpdateFacilityScopeLocaleRequest;
import com.example.resortbackendapplication1.facility.model.dto.FacilityScopeLocaleDto;
import com.example.resortbackendapplication1.facility.model.entity.FacilityScopeEntity;
import com.example.resortbackendapplication1.facility.model.entity.FacilityScopeLocaleEntity;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;

public interface FacilityScopeLocaleService {
    SuccessResponse create(CreateFacilityScopeLocaleRequest request,
                           FacilityScopeEntity facilityScopeEntity,
                           LocaleEntity localeEntity);

    FacilityScopeLocaleEntity getEntityById(Long facilityScopeId, Long id);

    PaginatedResponse<FacilityScopeLocaleDto> getAll(Long facilityScopeId, String localeCode, PaginatedRequest paginatedRequest);

    SuccessResponse update(FacilityScopeLocaleEntity entity,
                           UpdateFacilityScopeLocaleRequest request);

    SuccessResponse delete(FacilityScopeLocaleEntity entity);
}
