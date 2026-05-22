package com.example.resortbackendapplication1.service;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.dto.request.facilities.CreateFacilityRequest;
import com.example.resortbackendapplication1.dto.request.facilities.UpdateFacilityRequest;
import com.example.resortbackendapplication1.dto.response.facilities.FacilityResponse;
import com.example.resortbackendapplication1.model.entity.FacilityEntity;
import com.example.resortbackendapplication1.model.entity.FacilityGroupEntity;
import com.example.resortbackendapplication1.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.model.projection.FacilitySummary;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface FacilityService {

    SuccessResponse create(CreateFacilityRequest request,
                           Map<Long, LocaleEntity> localeEntityMap,
                           FacilityGroupEntity facilityGroupEntity);

    FacilityEntity getEntityById(Long facilityGroupId, Long id);

    FacilityResponse getById(Long facilityGroupId, Long id);

    PaginatedResponse<FacilitySummary> getAll(Long facilityGroupId, PaginatedRequest request);

    SuccessResponse update(FacilityEntity entity, UpdateFacilityRequest request);

    SuccessResponse delete(Long facilityGroupId, Long id);

    List<FacilityEntity> getAll(Set<Long> ids);
}
