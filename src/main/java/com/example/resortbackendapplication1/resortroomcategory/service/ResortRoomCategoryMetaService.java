package com.example.resortbackendapplication1.resortroomcategory.service;

import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.resortroomcategory.dto.request.meta.ResortRoomCategoryMetaRequest;
import com.example.resortbackendapplication1.resortroomcategory.model.entity.ResortRoomCategoryMetaEntity;
import com.example.resortbackendapplication1.unit.model.entity.UnitEntity;

public interface ResortRoomCategoryMetaService {

    ResortRoomCategoryMetaEntity getEntityByResortRoomCategoryId(Long resortRoomCategoryId);

    SuccessResponse update(ResortRoomCategoryMetaEntity entity,
                           ResortRoomCategoryMetaRequest request,
                           UnitEntity roomSizeUnit);
}
