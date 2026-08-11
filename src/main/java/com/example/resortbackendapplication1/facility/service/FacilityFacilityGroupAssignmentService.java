package com.example.resortbackendapplication1.facility.service;

import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.facility.model.entity.FacilityEntity;
import com.example.resortbackendapplication1.facility.model.entity.FacilityFacilityGroupAssignmentEntity;
import com.example.resortbackendapplication1.facility.model.entity.FacilityGroupEntity;

public interface FacilityFacilityGroupAssignmentService {

    SuccessResponse assign(FacilityEntity facilityEntity,
                           FacilityGroupEntity facilityGroupEntity);

    FacilityFacilityGroupAssignmentEntity getEntityByFacilityGroupId(Long facilityId, Long facilityGroupId);

    SuccessResponse unassign(FacilityFacilityGroupAssignmentEntity entity);
}
