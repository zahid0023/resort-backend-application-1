package com.example.resortbackendapplication1.resort.service;

import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.facility.model.entity.FacilityGroupEntity;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.resort.dto.request.resortfacilitygroup.CreateResortFacilityGroupRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortfacilitygroup.ResortFacilityGroupFilterRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortfacilitygroup.UpdateResortFacilityGroupRequest;
import com.example.resortbackendapplication1.resort.dto.response.ResortFacilityGroupResponse;
import com.example.resortbackendapplication1.resort.model.dto.ResortFacilityGroupDto;
import com.example.resortbackendapplication1.resort.model.entity.ResortEntity;
import com.example.resortbackendapplication1.resort.model.entity.ResortFacilityGroupEntity;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ResortFacilityGroupService {

    SuccessResponse create(CreateResortFacilityGroupRequest request,
                           ResortEntity resortEntity,
                           FacilityGroupEntity facilityGroupEntity,
                           Map<Long, LocaleEntity> localeEntityMap);

    ResortFacilityGroupEntity getEntityById(Long id, Long resortId);

    ResortFacilityGroupResponse getById(Long id, Long resortId);

    PaginatedResponse<ResortFacilityGroupDto> getAll(ResortFacilityGroupFilterRequest request, Long resortId);

    SuccessResponse update(ResortFacilityGroupEntity entity,
                           UpdateResortFacilityGroupRequest request,
                           FacilityGroupEntity facilityGroupEntity);

    SuccessResponse delete(Long id, Long resortId);

    List<ResortFacilityGroupEntity> getAll(Set<Long> ids);
}
