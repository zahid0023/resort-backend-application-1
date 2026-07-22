package com.example.resortbackendapplication1.facility.service;

import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.facility.dto.request.facilitygroups.CreateFacilityGroupRequest;
import com.example.resortbackendapplication1.facility.dto.request.facilitygroups.FacilityGroupFilterRequest;
import com.example.resortbackendapplication1.facility.dto.request.facilitygroups.UpdateFacilityGroupRequest;
import com.example.resortbackendapplication1.facility.dto.response.facilitygroups.FacilityGroupResponse;
import com.example.resortbackendapplication1.facility.model.dto.FacilityGroupDto;
import com.example.resortbackendapplication1.facility.model.entity.FacilityGroupEntity;
import com.example.resortbackendapplication1.facility.model.entity.FacilityScopeEntity;
import com.example.resortbackendapplication1.facility.model.enums.FacilityScopeCode;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface FacilityGroupService {

    SuccessResponse create(CreateFacilityGroupRequest request,
                           Map<Long, LocaleEntity> localeEntityMap,
                           List<FacilityScopeEntity> scopeEntities);

    FacilityGroupEntity getEntityById(Long id);

    FacilityGroupResponse getById(Long id);

    PaginatedResponse<FacilityGroupDto> getAll(FacilityGroupFilterRequest request, FacilityScopeCode scopeCode);

    SuccessResponse update(FacilityGroupEntity entity,
                           UpdateFacilityGroupRequest request);

    SuccessResponse delete(Long id);

    List<FacilityGroupEntity> getAll(Set<Long> ids);
}
