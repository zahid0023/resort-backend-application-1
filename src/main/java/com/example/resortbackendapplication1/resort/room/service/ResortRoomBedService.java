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
import com.example.resortbackendapplication1.resort.roomcategory.model.dto.ResortRoomCategoryBedDto;

import java.util.List;

public interface ResortRoomBedService {

    SuccessResponse create(CreateResortRoomBedRequest request,
                           ResortRoomEntity resortRoomEntity,
                           BedTypeEntity bedTypeEntity);

    ResortRoomBedEntity getEntityById(Long resortRoomId, Long id);

    ResortRoomBedResponse getById(Long resortRoomId, Long id);

    /**
     * If the room has zero own active bed rows, returns its category's beds instead (each entry's
     * {@code inherited} flag is {@code true}), ignoring {@code request}'s sort/search — same simplification
     * {@code ResortRoomPriceService.getAllGroupedByCurrency} makes for its category fallback.
     */
    PaginatedResponse<ResortRoomBedDto> getAll(Long resortRoomId, ResortRoomBedFilterRequest request,
                                               List<ResortRoomCategoryBedDto> resortRoomCategoryBedsFallback);

    SuccessResponse update(ResortRoomBedEntity entity, UpdateResortRoomBedRequest request);

    SuccessResponse delete(ResortRoomBedEntity entity);
}
