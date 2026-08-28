package com.example.resortbackendapplication1.resort.room.service;

import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.facility.model.entity.FacilityGroupEntity;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.resort.room.dto.request.resortroomfacilitygroup.CreateResortRoomFacilityGroupRequest;
import com.example.resortbackendapplication1.resort.room.dto.request.resortroomfacilitygroup.ResortRoomFacilityGroupFilterRequest;
import com.example.resortbackendapplication1.resort.room.dto.request.resortroomfacilitygroup.UpdateResortRoomFacilityGroupRequest;
import com.example.resortbackendapplication1.resort.room.dto.response.resortroomfacilitygroups.ResortRoomFacilityGroupResponse;
import com.example.resortbackendapplication1.resort.room.model.dto.ResortRoomFacilityGroupDto;
import com.example.resortbackendapplication1.resort.room.model.entity.ResortRoomEntity;
import com.example.resortbackendapplication1.resort.room.model.entity.ResortRoomFacilityGroupEntity;

public interface ResortRoomFacilityGroupService {

    SuccessResponse create(CreateResortRoomFacilityGroupRequest request,
                           ResortRoomEntity resortRoomEntity,
                           FacilityGroupEntity facilityGroupEntity,
                           LocaleEntity localeEntity);

    ResortRoomFacilityGroupEntity getEntityById(Long resortRoomId, Long id);

    ResortRoomFacilityGroupResponse getById(Long resortRoomId, Long id);

    PaginatedResponse<ResortRoomFacilityGroupDto> getAll(Long resortRoomId, ResortRoomFacilityGroupFilterRequest request);

    SuccessResponse update(ResortRoomFacilityGroupEntity entity,
                           UpdateResortRoomFacilityGroupRequest request);

    SuccessResponse delete(ResortRoomFacilityGroupEntity entity);
}
