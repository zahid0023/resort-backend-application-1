package com.example.resortbackendapplication1.resort.service;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.resort.dto.request.resortroomcategory.locale.CreateResortRoomCategoryLocaleRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortroomcategory.locale.UpdateResortRoomCategoryLocaleRequest;
import com.example.resortbackendapplication1.resort.model.dto.ResortRoomCategoryLocaleDto;
import com.example.resortbackendapplication1.resort.model.entity.ResortRoomCategoryEntity;
import com.example.resortbackendapplication1.resort.model.entity.ResortRoomCategoryLocaleEntity;

public interface ResortRoomCategoryLocaleService {

    SuccessResponse create(CreateResortRoomCategoryLocaleRequest request,
                           ResortRoomCategoryEntity resortRoomCategoryEntity,
                           LocaleEntity localeEntity);

    ResortRoomCategoryLocaleEntity getEntityById(Long resortRoomCategoryId, Long id);

    PaginatedResponse<ResortRoomCategoryLocaleDto> getAll(Long resortRoomCategoryId, String localeCode, PaginatedRequest paginatedRequest);

    SuccessResponse update(ResortRoomCategoryLocaleEntity entity, UpdateResortRoomCategoryLocaleRequest request);

    SuccessResponse delete(ResortRoomCategoryLocaleEntity entity);
}
