package com.example.resortbackendapplication1.facility.service;

import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.facility.dto.request.facilityscope.CreateFacilityScopeRequest;
import com.example.resortbackendapplication1.facility.dto.request.facilityscope.FacilityScopeFilterRequest;
import com.example.resortbackendapplication1.facility.dto.request.facilityscope.UpdateFacilityScopeRequest;
import com.example.resortbackendapplication1.facility.dto.response.facilityscopes.FacilityScopeCountResponse;
import com.example.resortbackendapplication1.facility.dto.response.facilityscopes.FacilityScopeResponse;
import com.example.resortbackendapplication1.facility.model.dto.FacilityScopeDto;
import com.example.resortbackendapplication1.facility.model.entity.FacilityScopeEntity;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;

import java.util.List;
import java.util.Set;

public interface FacilityScopeService {

    SuccessResponse create(CreateFacilityScopeRequest request,
                           LocaleEntity localeEntity);

    FacilityScopeEntity getEntityById(Long id);

    List<FacilityScopeEntity> getAll(Set<Long> ids);

    List<FacilityScopeEntity> getAllByCodes(Set<String> codes);

    FacilityScopeResponse getById(Long id);

    PaginatedResponse<FacilityScopeDto> getAll(FacilityScopeFilterRequest request);

    FacilityScopeCountResponse getActiveCount();

    SuccessResponse update(FacilityScopeEntity entity,
                           UpdateFacilityScopeRequest request);

    SuccessResponse delete(FacilityScopeEntity entity);
}
