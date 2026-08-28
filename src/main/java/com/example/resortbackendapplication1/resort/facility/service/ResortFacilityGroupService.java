package com.example.resortbackendapplication1.resort.facility.service;

import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.facility.model.entity.FacilityGroupEntity;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.resort.facility.dto.request.resortfacilitygroup.CreateResortFacilityGroupRequest;
import com.example.resortbackendapplication1.resort.facility.dto.request.resortfacilitygroup.ResortFacilityGroupFilterRequest;
import com.example.resortbackendapplication1.resort.facility.dto.request.resortfacilitygroup.UpdateResortFacilityGroupRequest;
import com.example.resortbackendapplication1.resort.facility.dto.response.resortfacilitygroups.ResortFacilityGroupResponse;
import com.example.resortbackendapplication1.resort.facility.model.dto.ResortFacilityGroupDto;
import com.example.resortbackendapplication1.resort.core.model.entity.ResortEntity;
import com.example.resortbackendapplication1.resort.facility.model.entity.ResortFacilityGroupEntity;

public interface ResortFacilityGroupService {

    SuccessResponse create(CreateResortFacilityGroupRequest request,
                           ResortEntity resortEntity,
                           FacilityGroupEntity facilityGroupEntity,
                           LocaleEntity localeEntity);

    ResortFacilityGroupEntity getEntityById(Long resortId, Long id);

    ResortFacilityGroupResponse getById(Long resortId, Long id);

    PaginatedResponse<ResortFacilityGroupDto> getAll(Long resortId, ResortFacilityGroupFilterRequest request);

    SuccessResponse update(ResortFacilityGroupEntity entity,
                           UpdateResortFacilityGroupRequest request);

    SuccessResponse delete(ResortFacilityGroupEntity entity);
}
