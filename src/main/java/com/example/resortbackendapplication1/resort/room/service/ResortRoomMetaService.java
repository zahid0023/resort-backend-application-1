package com.example.resortbackendapplication1.resort.room.service;

import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.resort.room.dto.request.resortroommeta.UpdateResortRoomMetaRequest;
import com.example.resortbackendapplication1.resort.room.model.entity.ResortRoomMetaEntity;
import com.example.resortbackendapplication1.unit.model.entity.UnitEntity;

public interface ResortRoomMetaService {

    ResortRoomMetaEntity getEntityByResortRoomId(Long resortRoomId);

    SuccessResponse update(ResortRoomMetaEntity entity,
                           UpdateResortRoomMetaRequest request,
                           UnitEntity roomSizeUnitEntity);
}
