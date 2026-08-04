package com.example.resortbackendapplication1.facility.service;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.facility.model.dto.FacilityGroupScopeAssignmentDto;
import com.example.resortbackendapplication1.facility.model.entity.FacilityGroupEntity;
import com.example.resortbackendapplication1.facility.model.entity.FacilityGroupScopeAssignmentEntity;
import com.example.resortbackendapplication1.facility.model.entity.FacilityScopeEntity;

public interface FacilityGroupScopeAssignmentService {

    SuccessResponse assign(FacilityScopeEntity facilityScopeEntity,
                           FacilityGroupEntity facilityGroupEntity);

    FacilityGroupScopeAssignmentEntity getEntityById(Long facilityScopeId, Long id);

    PaginatedResponse<FacilityGroupScopeAssignmentDto> getAll(Long facilityScopeId, PaginatedRequest paginatedRequest);

    SuccessResponse unassign(FacilityGroupScopeAssignmentEntity entity);
}
