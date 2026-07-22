package com.example.resortbackendapplication1.facility.service;

import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.facility.model.dto.FacilityGroupScopeAssignmentDto;
import com.example.resortbackendapplication1.facility.model.entity.FacilityGroupEntity;
import com.example.resortbackendapplication1.facility.model.entity.FacilityGroupScopeAssignmentEntity;
import com.example.resortbackendapplication1.facility.model.entity.FacilityScopeEntity;

import java.util.List;

public interface FacilityGroupScopeAssignmentService {

    SuccessResponse assign(FacilityGroupEntity facilityGroupEntity, FacilityScopeEntity facilityScopeEntity);

    SuccessResponse unassign(Long facilityGroupId, Long facilityScopeId);

    List<FacilityGroupScopeAssignmentDto> getAll(Long facilityGroupId);

    FacilityGroupScopeAssignmentEntity getEntityById(Long facilityGroupId, Long facilityScopeId);
}
