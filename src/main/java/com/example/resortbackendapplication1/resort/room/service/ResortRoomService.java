package com.example.resortbackendapplication1.resort.room.service;

import com.example.resortbackendapplication1.bedtype.model.entity.BedTypeEntity;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.resort.room.dto.request.resortroom.CreateResortRoomRequest;
import com.example.resortbackendapplication1.resort.room.dto.request.resortroom.ResortRoomFilterRequest;
import com.example.resortbackendapplication1.resort.room.dto.request.resortroom.UpdateResortRoomRequest;
import com.example.resortbackendapplication1.resort.room.dto.response.resortrooms.ResortRoomResponse;
import com.example.resortbackendapplication1.resort.room.model.dto.ResortRoomDto;
import com.example.resortbackendapplication1.resort.roomcategory.model.entity.ResortRoomCategoryEntity;
import com.example.resortbackendapplication1.resort.room.model.entity.ResortRoomEntity;
import com.example.resortbackendapplication1.roomstatus.model.entity.RoomStatusEntity;
import com.example.resortbackendapplication1.unit.model.entity.UnitEntity;

import java.util.List;

public interface ResortRoomService {

    SuccessResponse create(CreateResortRoomRequest request,
                           ResortRoomCategoryEntity resortRoomCategoryEntity,
                           RoomStatusEntity roomStatusEntity,
                           LocaleEntity localeEntity,
                           UnitEntity roomSizeUnitEntity,
                           List<BedTypeEntity> bedTypeEntities);

    ResortRoomEntity getEntityById(Long resortRoomCategoryId, Long id);

    ResortRoomResponse getById(Long resortRoomCategoryId, Long id);

    PaginatedResponse<ResortRoomDto> getAll(Long resortRoomCategoryId, ResortRoomFilterRequest request);

    SuccessResponse update(ResortRoomEntity entity, UpdateResortRoomRequest request);

    /** Room status transitions are deliberately kept out of {@link #update}; this is their only entry point. */
    SuccessResponse updateStatus(ResortRoomEntity entity, RoomStatusEntity roomStatusEntity);

    SuccessResponse delete(ResortRoomEntity entity);
}
