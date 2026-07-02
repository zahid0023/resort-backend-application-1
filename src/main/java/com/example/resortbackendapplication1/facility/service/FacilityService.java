package com.example.resortbackendapplication1.facility.service;

import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.facility.dto.request.facilities.CreateFacilityRequest;
import com.example.resortbackendapplication1.facility.dto.request.facilities.FacilityFilterRequest;
import com.example.resortbackendapplication1.facility.dto.request.facilities.UpdateFacilityRequest;
import com.example.resortbackendapplication1.facility.dto.response.facilities.FacilityResponse;
import com.example.resortbackendapplication1.facility.model.dto.FacilityDto;
import com.example.resortbackendapplication1.facility.model.entity.FacilityEntity;
import com.example.resortbackendapplication1.facility.model.entity.FacilityGroupEntity;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface FacilityService {

    SuccessResponse create(CreateFacilityRequest request,
                           Map<Long, LocaleEntity> localeEntityMap,
                           FacilityGroupEntity facilityGroupEntity);

    FacilityEntity getEntityById(Long id);

    FacilityResponse getById(Long id);

    PaginatedResponse<FacilityDto> getAll(FacilityFilterRequest request, Long facilityGroupId);

    SuccessResponse update(FacilityEntity entity, UpdateFacilityRequest request);

    SuccessResponse delete(Long id);

    List<FacilityEntity> getAll(Set<Long> ids);
}
