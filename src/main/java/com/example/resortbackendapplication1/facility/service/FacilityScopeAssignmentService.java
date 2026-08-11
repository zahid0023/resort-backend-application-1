package com.example.resortbackendapplication1.facility.service;

import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.facility.model.entity.FacilityEntity;
import com.example.resortbackendapplication1.facility.model.entity.FacilityScopeAssignmentEntity;
import com.example.resortbackendapplication1.facility.model.entity.FacilityScopeEntity;

public interface FacilityScopeAssignmentService {

    SuccessResponse assign(FacilityEntity facilityEntity,
                           FacilityScopeEntity facilityScopeEntity);

    FacilityScopeAssignmentEntity getEntityByFacilityScopeId(Long facilityId, Long facilityScopeId);

    SuccessResponse unassign(FacilityScopeAssignmentEntity entity);
}
