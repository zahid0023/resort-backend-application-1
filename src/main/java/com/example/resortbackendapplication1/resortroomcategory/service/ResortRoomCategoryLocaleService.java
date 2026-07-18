package com.example.resortbackendapplication1.resortroomcategory.service;

import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.resortroomcategory.dto.request.locale.CreateResortRoomCategoryLocaleRequest;
import com.example.resortbackendapplication1.resortroomcategory.dto.request.locale.UpdateResortRoomCategoryLocaleRequest;
import com.example.resortbackendapplication1.resortroomcategory.model.entity.ResortRoomCategoryEntity;
import com.example.resortbackendapplication1.resortroomcategory.model.entity.ResortRoomCategoryLocaleEntity;

public interface ResortRoomCategoryLocaleService {

    SuccessResponse create(ResortRoomCategoryEntity resortRoomCategoryEntity,
                           LocaleEntity localeEntity,
                           CreateResortRoomCategoryLocaleRequest request);

    ResortRoomCategoryLocaleEntity getEntityById(Long resortRoomCategoryId, Long id);

    SuccessResponse update(ResortRoomCategoryLocaleEntity entity, UpdateResortRoomCategoryLocaleRequest request);

    SuccessResponse delete(ResortRoomCategoryLocaleEntity entity);
}
