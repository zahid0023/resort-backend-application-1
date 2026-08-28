package com.example.resortbackendapplication1.resort.roomcategory.service;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.resort.roomcategory.dto.request.resortroomcategory.locale.CreateResortRoomCategoryLocaleRequest;
import com.example.resortbackendapplication1.resort.roomcategory.dto.request.resortroomcategory.locale.UpdateResortRoomCategoryLocaleRequest;
import com.example.resortbackendapplication1.resort.roomcategory.dto.response.resortroomcategorylocales.ResortRoomCategoryLocaleCountResponse;
import com.example.resortbackendapplication1.resort.roomcategory.model.dto.ResortRoomCategoryLocaleDto;
import com.example.resortbackendapplication1.resort.roomcategory.model.entity.ResortRoomCategoryEntity;
import com.example.resortbackendapplication1.resort.roomcategory.model.entity.ResortRoomCategoryLocaleEntity;

public interface ResortRoomCategoryLocaleService {

    SuccessResponse create(CreateResortRoomCategoryLocaleRequest request,
                           ResortRoomCategoryEntity resortRoomCategoryEntity,
                           LocaleEntity localeEntity);

    ResortRoomCategoryLocaleEntity getEntityById(Long resortRoomCategoryId, Long id);

    PaginatedResponse<ResortRoomCategoryLocaleDto> getAll(Long resortRoomCategoryId, String localeCode, PaginatedRequest paginatedRequest);

    /**
     * The count and codes of platform {@code Locale} entries this resort room category already has an active
     * translation for — matched via {@code locale_id}, so the frontend can gray out locales already present
     * before calling {@link #create}, which returns {@code 409 CONFLICT} for a duplicate.
     */
    ResortRoomCategoryLocaleCountResponse getActiveCount(Long resortRoomCategoryId);

    SuccessResponse update(ResortRoomCategoryLocaleEntity entity, UpdateResortRoomCategoryLocaleRequest request);

    SuccessResponse delete(ResortRoomCategoryLocaleEntity entity);
}
