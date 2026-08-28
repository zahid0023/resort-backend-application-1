package com.example.resortbackendapplication1.resort.room.service;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.resort.room.dto.request.resortroomfacility.locale.CreateResortRoomFacilityLocaleRequest;
import com.example.resortbackendapplication1.resort.room.dto.request.resortroomfacility.locale.UpdateResortRoomFacilityLocaleRequest;
import com.example.resortbackendapplication1.resort.room.dto.response.resortroomfacilitylocales.ResortRoomFacilityLocaleCountResponse;
import com.example.resortbackendapplication1.resort.room.model.dto.ResortRoomFacilityLocaleDto;
import com.example.resortbackendapplication1.resort.room.model.entity.ResortRoomFacilityEntity;
import com.example.resortbackendapplication1.resort.room.model.entity.ResortRoomFacilityLocaleEntity;

public interface ResortRoomFacilityLocaleService {

    SuccessResponse create(CreateResortRoomFacilityLocaleRequest request,
                           ResortRoomFacilityEntity resortRoomFacilityEntity,
                           LocaleEntity localeEntity);

    ResortRoomFacilityLocaleEntity getEntityById(Long resortRoomFacilityId, Long id);

    PaginatedResponse<ResortRoomFacilityLocaleDto> getAll(Long resortRoomFacilityId, String localeCode, PaginatedRequest paginatedRequest);

    ResortRoomFacilityLocaleCountResponse getActiveCount(Long resortRoomFacilityId);

    SuccessResponse update(ResortRoomFacilityLocaleEntity entity,
                           UpdateResortRoomFacilityLocaleRequest request);

    SuccessResponse delete(ResortRoomFacilityLocaleEntity entity);
}
