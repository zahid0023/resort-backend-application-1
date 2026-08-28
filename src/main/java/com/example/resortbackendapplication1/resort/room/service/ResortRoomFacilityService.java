package com.example.resortbackendapplication1.resort.room.service;

import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.facility.model.entity.FacilityEntity;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.resort.room.dto.request.resortroomfacility.CreateResortRoomFacilityRequest;
import com.example.resortbackendapplication1.resort.room.dto.request.resortroomfacility.ResortRoomFacilityFilterRequest;
import com.example.resortbackendapplication1.resort.room.dto.request.resortroomfacility.UpdateResortRoomFacilityRequest;
import com.example.resortbackendapplication1.resort.room.dto.response.resortroomfacilities.ResortRoomFacilityResponse;
import com.example.resortbackendapplication1.resort.room.model.dto.ResortRoomFacilityDto;
import com.example.resortbackendapplication1.resort.room.model.entity.ResortRoomEntity;
import com.example.resortbackendapplication1.resort.room.model.entity.ResortRoomFacilityEntity;
import com.example.resortbackendapplication1.resort.room.model.entity.ResortRoomFacilityGroupEntity;

public interface ResortRoomFacilityService {

    SuccessResponse create(CreateResortRoomFacilityRequest request,
                           ResortRoomEntity resortRoomEntity,
                           ResortRoomFacilityGroupEntity resortRoomFacilityGroupEntity,
                           FacilityEntity facilityEntity,
                           LocaleEntity localeEntity);

    ResortRoomFacilityEntity getEntityById(Long resortRoomId, Long id);

    ResortRoomFacilityResponse getById(Long resortRoomId, Long id);

    PaginatedResponse<ResortRoomFacilityDto> getAll(Long resortRoomId, ResortRoomFacilityFilterRequest request);

    SuccessResponse update(ResortRoomFacilityEntity entity,
                           UpdateResortRoomFacilityRequest request);

    SuccessResponse delete(ResortRoomFacilityEntity entity);
}
