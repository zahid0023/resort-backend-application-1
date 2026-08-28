package com.example.resortbackendapplication1.resort.room.service;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.resort.room.dto.request.resortroomfacilitygroup.locale.CreateResortRoomFacilityGroupLocaleRequest;
import com.example.resortbackendapplication1.resort.room.dto.request.resortroomfacilitygroup.locale.UpdateResortRoomFacilityGroupLocaleRequest;
import com.example.resortbackendapplication1.resort.room.dto.response.resortroomfacilitygrouplocales.ResortRoomFacilityGroupLocaleCountResponse;
import com.example.resortbackendapplication1.resort.room.model.dto.ResortRoomFacilityGroupLocaleDto;
import com.example.resortbackendapplication1.resort.room.model.entity.ResortRoomFacilityGroupEntity;
import com.example.resortbackendapplication1.resort.room.model.entity.ResortRoomFacilityGroupLocaleEntity;

public interface ResortRoomFacilityGroupLocaleService {

    SuccessResponse create(CreateResortRoomFacilityGroupLocaleRequest request,
                           ResortRoomFacilityGroupEntity resortRoomFacilityGroupEntity,
                           LocaleEntity localeEntity);

    ResortRoomFacilityGroupLocaleEntity getEntityById(Long resortRoomFacilityGroupId, Long id);

    PaginatedResponse<ResortRoomFacilityGroupLocaleDto> getAll(Long resortRoomFacilityGroupId, String localeCode, PaginatedRequest paginatedRequest);

    ResortRoomFacilityGroupLocaleCountResponse getActiveCount(Long resortRoomFacilityGroupId);

    SuccessResponse update(ResortRoomFacilityGroupLocaleEntity entity,
                           UpdateResortRoomFacilityGroupLocaleRequest request);

    SuccessResponse delete(ResortRoomFacilityGroupLocaleEntity entity);
}
