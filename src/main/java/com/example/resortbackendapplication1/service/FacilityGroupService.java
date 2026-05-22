package com.example.resortbackendapplication1.service;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.dto.request.facilitygroups.CreateFacilityGroupRequest;
import com.example.resortbackendapplication1.dto.request.facilitygroups.UpdateFacilityGroupRequest;
import com.example.resortbackendapplication1.dto.response.facilitygroups.FacilityGroupResponse;
import com.example.resortbackendapplication1.model.entity.FacilityGroupEntity;
import com.example.resortbackendapplication1.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.model.projection.FacilityGroupSummary;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface FacilityGroupService {

    SuccessResponse create(CreateFacilityGroupRequest request,
                           Map<Long, LocaleEntity> localeEntityMap);

    FacilityGroupEntity getEntityById(Long id);

    FacilityGroupResponse getById(Long id);

    PaginatedResponse<FacilityGroupSummary> getAll(PaginatedRequest request);

    SuccessResponse update(FacilityGroupEntity entity,
                           UpdateFacilityGroupRequest request);

    SuccessResponse delete(Long id);

    List<FacilityGroupEntity> getAll(Set<Long> ids);
}
