package com.example.resortbackendapplication1.resort.roomcategory.service;

import com.example.resortbackendapplication1.bedtype.model.entity.BedTypeEntity;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.resort.roomcategory.dto.request.resortroomcategorybed.CreateResortRoomCategoryBedRequest;
import com.example.resortbackendapplication1.resort.roomcategory.dto.request.resortroomcategorybed.ResortRoomCategoryBedFilterRequest;
import com.example.resortbackendapplication1.resort.roomcategory.dto.request.resortroomcategorybed.UpdateResortRoomCategoryBedRequest;
import com.example.resortbackendapplication1.resort.roomcategory.dto.response.resortroomcategorybeds.ResortRoomCategoryBedResponse;
import com.example.resortbackendapplication1.resort.roomcategory.model.dto.ResortRoomCategoryBedDto;
import com.example.resortbackendapplication1.resort.roomcategory.model.entity.ResortRoomCategoryBedEntity;
import com.example.resortbackendapplication1.resort.roomcategory.model.entity.ResortRoomCategoryEntity;

import java.util.List;

public interface ResortRoomCategoryBedService {

    SuccessResponse create(CreateResortRoomCategoryBedRequest request,
                           ResortRoomCategoryEntity resortRoomCategoryEntity,
                           BedTypeEntity bedTypeEntity);

    ResortRoomCategoryBedEntity getEntityById(Long resortRoomCategoryId, Long id);

    ResortRoomCategoryBedResponse getById(Long resortRoomCategoryId, Long id);

    PaginatedResponse<ResortRoomCategoryBedDto> getAll(Long resortRoomCategoryId, ResortRoomCategoryBedFilterRequest request);

    /** Every active bed row for the category, unpaginated — used as the room-level fallback bundle when a room has no own beds. */
    List<ResortRoomCategoryBedDto> getAllActive(Long resortRoomCategoryId);

    SuccessResponse update(ResortRoomCategoryBedEntity entity, UpdateResortRoomCategoryBedRequest request);

    SuccessResponse delete(ResortRoomCategoryBedEntity entity);
}
