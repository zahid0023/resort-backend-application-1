package com.example.resortbackendapplication1.facility.service;

import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.facility.dto.request.facilitygroup.CreateFacilityGroupRequest;
import com.example.resortbackendapplication1.facility.dto.request.facilitygroup.FacilityGroupFilterRequest;
import com.example.resortbackendapplication1.facility.dto.request.facilitygroup.UpdateFacilityGroupRequest;
import com.example.resortbackendapplication1.facility.dto.response.facilitygroups.FacilityGroupCountResponse;
import com.example.resortbackendapplication1.facility.dto.response.facilitygroups.FacilityGroupResponse;
import com.example.resortbackendapplication1.facility.model.dto.FacilityGroupDto;
import com.example.resortbackendapplication1.facility.model.entity.FacilityGroupEntity;
import com.example.resortbackendapplication1.facility.model.entity.FacilityScopeEntity;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;

import java.util.List;
import java.util.Set;

public interface FacilityGroupService {

    SuccessResponse create(CreateFacilityGroupRequest request,
                           List<FacilityScopeEntity> facilityScopeEntities,
                           LocaleEntity localeEntity);

    FacilityGroupEntity getEntityById(Long id);

    List<FacilityGroupEntity> getAll(Set<Long> ids);

    FacilityGroupResponse getById(Long id);

    PaginatedResponse<FacilityGroupDto> getAll(FacilityGroupFilterRequest request);

    FacilityGroupCountResponse getCount(List<FacilityScopeEntity> facilityScopeEntities);

    SuccessResponse update(FacilityGroupEntity entity,
                           UpdateFacilityGroupRequest request);

    SuccessResponse delete(FacilityGroupEntity entity);
}
