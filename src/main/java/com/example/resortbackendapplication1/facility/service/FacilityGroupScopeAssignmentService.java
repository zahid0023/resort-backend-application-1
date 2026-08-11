package com.example.resortbackendapplication1.facility.service;

import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.facility.model.entity.FacilityGroupEntity;
import com.example.resortbackendapplication1.facility.model.entity.FacilityGroupScopeAssignmentEntity;
import com.example.resortbackendapplication1.facility.model.entity.FacilityScopeEntity;

public interface FacilityGroupScopeAssignmentService {

    SuccessResponse assign(FacilityGroupEntity facilityGroupEntity,
                           FacilityScopeEntity facilityScopeEntity);

    FacilityGroupScopeAssignmentEntity getEntityByFacilityScopeId(Long facilityGroupId, Long facilityScopeId);

    SuccessResponse unassign(FacilityGroupScopeAssignmentEntity entity);
}
