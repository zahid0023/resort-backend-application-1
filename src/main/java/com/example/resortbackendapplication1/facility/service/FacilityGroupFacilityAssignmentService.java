package com.example.resortbackendapplication1.facility.service;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.facility.model.dto.FacilityGroupFacilityAssignmentDto;
import com.example.resortbackendapplication1.facility.model.entity.FacilityEntity;
import com.example.resortbackendapplication1.facility.model.entity.FacilityGroupEntity;
import com.example.resortbackendapplication1.facility.model.entity.FacilityGroupFacilityAssignmentEntity;

public interface FacilityGroupFacilityAssignmentService {

    SuccessResponse assign(FacilityGroupEntity facilityGroupEntity,
                           FacilityEntity facilityEntity);

    FacilityGroupFacilityAssignmentEntity getEntityById(Long facilityGroupId, Long id);

    PaginatedResponse<FacilityGroupFacilityAssignmentDto> getAll(Long facilityGroupId, PaginatedRequest paginatedRequest);

    SuccessResponse unassign(FacilityGroupFacilityAssignmentEntity entity);
}
