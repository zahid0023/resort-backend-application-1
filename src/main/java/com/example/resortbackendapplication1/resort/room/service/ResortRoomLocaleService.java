package com.example.resortbackendapplication1.resort.room.service;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.resort.room.dto.request.resortroom.locale.CreateResortRoomLocaleRequest;
import com.example.resortbackendapplication1.resort.room.dto.request.resortroom.locale.UpdateResortRoomLocaleRequest;
import com.example.resortbackendapplication1.resort.room.dto.response.resortroomlocales.ResortRoomLocaleCountResponse;
import com.example.resortbackendapplication1.resort.room.model.dto.ResortRoomLocaleDto;
import com.example.resortbackendapplication1.resort.room.model.entity.ResortRoomEntity;
import com.example.resortbackendapplication1.resort.room.model.entity.ResortRoomLocaleEntity;

public interface ResortRoomLocaleService {

    SuccessResponse create(CreateResortRoomLocaleRequest request,
                           ResortRoomEntity resortRoomEntity,
                           LocaleEntity localeEntity);

    ResortRoomLocaleEntity getEntityById(Long resortRoomId, Long id);

    PaginatedResponse<ResortRoomLocaleDto> getAll(Long resortRoomId, String localeCode, PaginatedRequest paginatedRequest);

    /**
     * The count and codes of platform {@code Locale} entries this resort room already has an active
     * translation for — matched via {@code locale_id}, so the frontend can gray out locales already present
     * before calling {@link #create}, which returns {@code 409 CONFLICT} for a duplicate.
     */
    ResortRoomLocaleCountResponse getActiveCount(Long resortRoomId);

    SuccessResponse update(ResortRoomLocaleEntity entity, UpdateResortRoomLocaleRequest request);

    SuccessResponse delete(ResortRoomLocaleEntity entity);
}
