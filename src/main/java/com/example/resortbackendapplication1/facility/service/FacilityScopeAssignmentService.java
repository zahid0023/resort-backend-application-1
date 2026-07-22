package com.example.resortbackendapplication1.facility.service;

import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.facility.model.dto.FacilityScopeAssignmentDto;
import com.example.resortbackendapplication1.facility.model.entity.FacilityEntity;
import com.example.resortbackendapplication1.facility.model.entity.FacilityScopeAssignmentEntity;
import com.example.resortbackendapplication1.facility.model.entity.FacilityScopeEntity;

import java.util.List;

public interface FacilityScopeAssignmentService {

    SuccessResponse assign(FacilityEntity facilityEntity, FacilityScopeEntity facilityScopeEntity);

    SuccessResponse unassign(Long facilityId, Long facilityScopeId);

    List<FacilityScopeAssignmentDto> getAll(Long facilityId);

    FacilityScopeAssignmentEntity getEntityById(Long facilityId, Long facilityScopeId);
}
