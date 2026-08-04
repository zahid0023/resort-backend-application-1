package com.example.resortbackendapplication1.facility.service;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.facility.model.dto.FacilityScopeAssignmentDto;
import com.example.resortbackendapplication1.facility.model.entity.FacilityEntity;
import com.example.resortbackendapplication1.facility.model.entity.FacilityScopeAssignmentEntity;
import com.example.resortbackendapplication1.facility.model.entity.FacilityScopeEntity;

public interface FacilityScopeAssignmentService {

    SuccessResponse assign(FacilityScopeEntity facilityScopeEntity,
                           FacilityEntity facilityEntity);

    FacilityScopeAssignmentEntity getEntityById(Long facilityScopeId, Long id);

    PaginatedResponse<FacilityScopeAssignmentDto> getAll(Long facilityScopeId, PaginatedRequest paginatedRequest);

    SuccessResponse unassign(FacilityScopeAssignmentEntity entity);
}
