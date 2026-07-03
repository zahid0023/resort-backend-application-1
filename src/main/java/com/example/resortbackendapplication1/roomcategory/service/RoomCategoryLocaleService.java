package com.example.resortbackendapplication1.roomcategory.service;

import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.roomcategory.dto.request.roomcategory.roomcategorylocale.CreateRoomCategoryLocaleRequest;
import com.example.resortbackendapplication1.roomcategory.dto.request.roomcategory.roomcategorylocale.UpdateRoomCategoryLocaleRequest;
import com.example.resortbackendapplication1.roomcategory.model.entity.RoomCategoryEntity;
import com.example.resortbackendapplication1.roomcategory.model.entity.RoomCategoryLocaleEntity;

public interface RoomCategoryLocaleService {

    SuccessResponse create(RoomCategoryEntity roomCategoryEntity,
                           LocaleEntity localeEntity,
                           CreateRoomCategoryLocaleRequest request);

    RoomCategoryLocaleEntity getEntityById(Long roomCategoryId, Long id);

    SuccessResponse update(RoomCategoryLocaleEntity entity, UpdateRoomCategoryLocaleRequest request);

    SuccessResponse delete(RoomCategoryLocaleEntity entity);
}
