package com.example.resortbackendapplication1.service;

import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.dto.request.roomcategories.roomcategorylocale.CreateRoomCategoryLocaleRequest;
import com.example.resortbackendapplication1.dto.request.roomcategories.roomcategorylocale.UpdateRoomCategoryLocaleRequest;
import com.example.resortbackendapplication1.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.model.entity.RoomCategoryEntity;
import com.example.resortbackendapplication1.model.entity.RoomCategoryLocaleEntity;

public interface RoomCategoryLocaleService {

    SuccessResponse create(RoomCategoryEntity roomCategoryEntity,
                           LocaleEntity localeEntity,
                           CreateRoomCategoryLocaleRequest request);

    RoomCategoryLocaleEntity getEntityById(Long roomCategoryId, Long id);

    SuccessResponse update(RoomCategoryLocaleEntity entity,
                           UpdateRoomCategoryLocaleRequest request);

    SuccessResponse delete(RoomCategoryLocaleEntity entity);
}
