package com.example.resortapplication1.service;

import com.example.resortapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortapplication1.commons.dto.response.SuccessResponse;
import com.example.resortapplication1.dto.request.facilitygroups.CreateFacilityGroupRequest;
import com.example.resortapplication1.dto.request.facilitygroups.UpdateFacilityGroupRequest;
import com.example.resortapplication1.dto.response.facilitygroups.FacilityGroupResponse;
import com.example.resortapplication1.model.dto.FacilityGroupDto;
import com.example.resortapplication1.model.entity.FacilityGroupEntity;
import org.springframework.data.domain.Pageable;

public interface FacilityGroupService {

    SuccessResponse createFacilityGroup(CreateFacilityGroupRequest request);

    FacilityGroupEntity getFacilityGroupEntity(Long id);

    FacilityGroupResponse getFacilityGroup(Long id);

    PaginatedResponse<FacilityGroupDto> getAllFacilityGroups(Pageable pageable);

    SuccessResponse updateFacilityGroup(Long id, UpdateFacilityGroupRequest request);

    SuccessResponse deleteFacilityGroup(Long id);
}
