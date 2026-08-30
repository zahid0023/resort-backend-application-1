package com.example.resortbackendapplication1.resort.room.service;

import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.resort.room.dto.request.resortroommeta.CreateResortRoomMetaRequest;
import com.example.resortbackendapplication1.resort.room.dto.request.resortroommeta.UpdateResortRoomMetaRequest;
import com.example.resortbackendapplication1.resort.room.dto.response.resortroommeta.ResortRoomMetaResponse;
import com.example.resortbackendapplication1.resort.room.model.entity.ResortRoomEntity;
import com.example.resortbackendapplication1.resort.room.model.entity.ResortRoomMetaEntity;
import com.example.resortbackendapplication1.resort.roomcategory.model.dto.ResortRoomCategoryMetaDto;
import com.example.resortbackendapplication1.unit.model.entity.UnitEntity;

import java.util.Optional;

public interface ResortRoomMetaService {

    ResortRoomMetaEntity getEntityByResortRoomId(Long resortRoomId);

    Optional<ResortRoomMetaEntity> findEntityByResortRoomId(Long resortRoomId);

    ResortRoomMetaResponse getByResortRoomId(Long resortRoomId, ResortRoomCategoryMetaDto resortRoomCategoryMetaFallback);

    SuccessResponse create(CreateResortRoomMetaRequest request,
                           ResortRoomEntity resortRoomEntity,
                           UnitEntity roomSizeUnitEntity);

    SuccessResponse update(ResortRoomMetaEntity entity,
                           UpdateResortRoomMetaRequest request,
                           UnitEntity roomSizeUnitEntity);

    SuccessResponse delete(ResortRoomMetaEntity entity);
}
