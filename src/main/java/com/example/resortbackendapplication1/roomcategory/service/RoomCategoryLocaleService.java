package com.example.resortbackendapplication1.roomcategory.service;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.roomcategory.dto.request.roomcategory.locale.CreateRoomCategoryLocaleRequest;
import com.example.resortbackendapplication1.roomcategory.dto.request.roomcategory.locale.UpdateRoomCategoryLocaleRequest;
import com.example.resortbackendapplication1.roomcategory.model.dto.RoomCategoryLocaleDto;
import com.example.resortbackendapplication1.roomcategory.model.entity.RoomCategoryEntity;
import com.example.resortbackendapplication1.roomcategory.model.entity.RoomCategoryLocaleEntity;
import com.example.resortbackendapplication1.locale.dto.response.locales.LocaleCountResponse;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;

public interface RoomCategoryLocaleService {
    SuccessResponse create(CreateRoomCategoryLocaleRequest request,
                           RoomCategoryEntity roomCategoryEntity,
                           LocaleEntity localeEntity);

    RoomCategoryLocaleEntity getEntityById(Long roomCategoryId, Long id);

    PaginatedResponse<RoomCategoryLocaleDto> getAll(Long roomCategoryId, String localeCode, PaginatedRequest paginatedRequest);

    LocaleCountResponse getCount(Long roomCategoryId);

    SuccessResponse update(RoomCategoryLocaleEntity entity,
                           UpdateRoomCategoryLocaleRequest request);

    SuccessResponse delete(RoomCategoryLocaleEntity entity);
}
