package com.example.resortbackendapplication1.service;

import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.dto.request.facilitygroups.CreateFacilityGroupRequest;
import com.example.resortbackendapplication1.dto.request.facilitygroups.UpdateFacilityGroupRequest;
import com.example.resortbackendapplication1.dto.response.facilitygroups.FacilityGroupResponse;
import com.example.resortbackendapplication1.model.dto.FacilityGroupDto;
import com.example.resortbackendapplication1.model.entity.FacilityGroupEntity;
import org.springframework.data.domain.Pageable;

public interface FacilityGroupService {

    SuccessResponse createFacilityGroup(CreateFacilityGroupRequest request);

    FacilityGroupEntity getFacilityGroupEntity(Long id);

    FacilityGroupResponse getFacilityGroup(Long id);

    PaginatedResponse<FacilityGroupDto> getAllFacilityGroups(Pageable pageable);

    SuccessResponse updateFacilityGroup(Long id, UpdateFacilityGroupRequest request);

    SuccessResponse deleteFacilityGroup(Long id);
}
