package com.example.resortbackendapplication1.facility.service;

import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.facility.dto.request.facilityscopes.FacilityScopeFilterRequest;
import com.example.resortbackendapplication1.facility.dto.request.facilityscopes.UpdateFacilityScopeRequest;
import com.example.resortbackendapplication1.facility.dto.response.facilityscopes.FacilityScopeResponse;
import com.example.resortbackendapplication1.facility.model.dto.FacilityScopeDto;
import com.example.resortbackendapplication1.facility.model.entity.FacilityScopeEntity;

import java.util.List;
import java.util.Set;

public interface FacilityScopeService {

    FacilityScopeEntity getEntityById(Long id);

    FacilityScopeResponse getById(Long id);

    PaginatedResponse<FacilityScopeDto> getAll(FacilityScopeFilterRequest request);

    SuccessResponse update(FacilityScopeEntity entity, UpdateFacilityScopeRequest request);

    List<FacilityScopeEntity> getAll(Set<Long> ids);
}
