package com.example.resortbackendapplication1.resort.service;

import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.resort.dto.request.resortroomcategorymeta.UpdateResortRoomCategoryMetaRequest;
import com.example.resortbackendapplication1.resort.model.entity.ResortRoomCategoryMetaEntity;
import com.example.resortbackendapplication1.unit.model.entity.UnitEntity;

public interface ResortRoomCategoryMetaService {

    ResortRoomCategoryMetaEntity getEntityByResortRoomCategoryId(Long resortRoomCategoryId);

    SuccessResponse update(ResortRoomCategoryMetaEntity entity,
                           UpdateResortRoomCategoryMetaRequest request,
                           UnitEntity roomSizeUnitEntity);
}
