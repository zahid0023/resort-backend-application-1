package com.example.resortbackendapplication1.resort.room.service;

import com.example.resortbackendapplication1.bedtype.model.entity.BedTypeEntity;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.resort.room.dto.request.resortroombed.CreateResortRoomBedRequest;
import com.example.resortbackendapplication1.resort.room.dto.request.resortroombed.ResortRoomBedFilterRequest;
import com.example.resortbackendapplication1.resort.room.dto.request.resortroombed.UpdateResortRoomBedRequest;
import com.example.resortbackendapplication1.resort.room.dto.response.resortroombeds.ResortRoomBedResponse;
import com.example.resortbackendapplication1.resort.room.model.dto.ResortRoomBedDto;
import com.example.resortbackendapplication1.resort.room.model.entity.ResortRoomBedEntity;
import com.example.resortbackendapplication1.resort.room.model.entity.ResortRoomEntity;

public interface ResortRoomBedService {

    SuccessResponse create(CreateResortRoomBedRequest request,
                           ResortRoomEntity resortRoomEntity,
                           BedTypeEntity bedTypeEntity);

    ResortRoomBedEntity getEntityById(Long resortRoomId, Long id);

    ResortRoomBedResponse getById(Long resortRoomId, Long id);

    PaginatedResponse<ResortRoomBedDto> getAll(Long resortRoomId, ResortRoomBedFilterRequest request);

    SuccessResponse update(ResortRoomBedEntity entity, UpdateResortRoomBedRequest request);

    SuccessResponse delete(ResortRoomBedEntity entity);
}
