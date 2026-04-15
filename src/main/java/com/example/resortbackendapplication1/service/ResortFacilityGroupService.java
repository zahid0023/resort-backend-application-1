package com.example.resortbackendapplication1.service;

import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.dto.request.resortfacilitygroups.CreateResortFacilityGroupRequest;
import com.example.resortbackendapplication1.dto.request.resortfacilitygroups.UpdateResortFacilityGroupRequest;
import com.example.resortbackendapplication1.dto.response.resortfacilitygroups.ResortFacilityGroupResponse;
import com.example.resortbackendapplication1.model.dto.ResortFacilityGroupDto;
import com.example.resortbackendapplication1.model.entity.FacilityGroupEntity;
import com.example.resortbackendapplication1.model.entity.ResortEntity;
import com.example.resortbackendapplication1.model.entity.ResortFacilityGroupEntity;
import org.springframework.data.domain.Pageable;

public interface ResortFacilityGroupService {

    SuccessResponse createResortFacilityGroup(CreateResortFacilityGroupRequest request,
                                              ResortEntity resortEntity,
                                              FacilityGroupEntity facilityGroupEntity);

    ResortFacilityGroupEntity getResortFacilityGroupEntity(Long id);

    ResortFacilityGroupEntity getResortFacilityGroupEntity(Long resortId, Long id);

    ResortFacilityGroupResponse getResortFacilityGroup(Long resortId, Long id);

    PaginatedResponse<ResortFacilityGroupDto> getAllResortFacilityGroups(Long resortId, Pageable pageable);

    SuccessResponse updateResortFacilityGroup(ResortFacilityGroupEntity resortFacilityGroupEntity, UpdateResortFacilityGroupRequest request);

    SuccessResponse deleteResortFacilityGroup(Long resortId, Long id);
}
