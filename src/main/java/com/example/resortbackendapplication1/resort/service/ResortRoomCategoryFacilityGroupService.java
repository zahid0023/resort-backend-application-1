package com.example.resortbackendapplication1.resort.service;

import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.facility.model.entity.FacilityGroupEntity;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.resort.dto.request.resortroomcategoryfacilitygroup.CreateResortRoomCategoryFacilityGroupRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortroomcategoryfacilitygroup.ResortRoomCategoryFacilityGroupFilterRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortroomcategoryfacilitygroup.UpdateResortRoomCategoryFacilityGroupRequest;
import com.example.resortbackendapplication1.resort.dto.response.resortroomcategoryfacilitygroups.ResortRoomCategoryFacilityGroupResponse;
import com.example.resortbackendapplication1.resort.model.dto.ResortRoomCategoryFacilityGroupDto;
import com.example.resortbackendapplication1.resort.model.entity.ResortRoomCategoryEntity;
import com.example.resortbackendapplication1.resort.model.entity.ResortRoomCategoryFacilityGroupEntity;

public interface ResortRoomCategoryFacilityGroupService {

    SuccessResponse create(CreateResortRoomCategoryFacilityGroupRequest request,
                           ResortRoomCategoryEntity resortRoomCategoryEntity,
                           FacilityGroupEntity facilityGroupEntity,
                           LocaleEntity localeEntity);

    ResortRoomCategoryFacilityGroupEntity getEntityById(Long resortRoomCategoryId, Long id);

    ResortRoomCategoryFacilityGroupResponse getById(Long resortRoomCategoryId, Long id);

    PaginatedResponse<ResortRoomCategoryFacilityGroupDto> getAll(Long resortRoomCategoryId, ResortRoomCategoryFacilityGroupFilterRequest request);

    SuccessResponse update(ResortRoomCategoryFacilityGroupEntity entity,
                           UpdateResortRoomCategoryFacilityGroupRequest request);

    SuccessResponse delete(ResortRoomCategoryFacilityGroupEntity entity);
}
