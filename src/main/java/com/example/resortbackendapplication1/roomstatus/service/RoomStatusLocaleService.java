package com.example.resortbackendapplication1.roomstatus.service;

import com.example.resortbackendapplication1.roomstatus.dto.request.roomstatus.locale.CreateRoomStatusLocaleRequest;
import com.example.resortbackendapplication1.roomstatus.dto.request.roomstatus.locale.UpdateRoomStatusLocaleRequest;
import com.example.resortbackendapplication1.roomstatus.model.dto.RoomStatusLocaleDto;
import com.example.resortbackendapplication1.roomstatus.model.entity.RoomStatusEntity;
import com.example.resortbackendapplication1.roomstatus.model.entity.RoomStatusLocaleEntity;
import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.locale.dto.response.locales.LocaleCountResponse;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;

public interface RoomStatusLocaleService {
    SuccessResponse create(CreateRoomStatusLocaleRequest request,
                           RoomStatusEntity roomStatusEntity,
                           LocaleEntity localeEntity);

    RoomStatusLocaleEntity getEntityById(Long roomStatusId, Long id);

    PaginatedResponse<RoomStatusLocaleDto> getAll(Long roomStatusId, String localeCode, PaginatedRequest paginatedRequest);

    LocaleCountResponse getCount(Long roomStatusId);

    SuccessResponse update(RoomStatusLocaleEntity entity,
                           UpdateRoomStatusLocaleRequest request);

    SuccessResponse delete(RoomStatusLocaleEntity entity);
}
