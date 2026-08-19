package com.example.resortbackendapplication1.resort.service;

import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.facility.model.entity.FacilityGroupEntity;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.resort.dto.request.resortfacilitygroup.CreateResortFacilityGroupRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortfacilitygroup.ResortFacilityGroupFilterRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortfacilitygroup.UpdateResortFacilityGroupRequest;
import com.example.resortbackendapplication1.resort.dto.response.resortfacilitygroups.ResortFacilityGroupResponse;
import com.example.resortbackendapplication1.resort.model.dto.ResortFacilityGroupDto;
import com.example.resortbackendapplication1.resort.model.entity.ResortEntity;
import com.example.resortbackendapplication1.resort.model.entity.ResortFacilityGroupEntity;

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
