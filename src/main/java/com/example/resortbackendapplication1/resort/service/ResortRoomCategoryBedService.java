package com.example.resortbackendapplication1.resort.service;

import com.example.resortbackendapplication1.bedtype.model.entity.BedTypeEntity;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.resort.dto.request.resortroomcategorybed.CreateResortRoomCategoryBedRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortroomcategorybed.ResortRoomCategoryBedFilterRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortroomcategorybed.UpdateResortRoomCategoryBedRequest;
import com.example.resortbackendapplication1.resort.dto.response.resortroomcategorybeds.ResortRoomCategoryBedResponse;
import com.example.resortbackendapplication1.resort.model.dto.ResortRoomCategoryBedDto;
import com.example.resortbackendapplication1.resort.model.entity.ResortRoomCategoryBedEntity;
import com.example.resortbackendapplication1.resort.model.entity.ResortRoomCategoryEntity;

public interface ResortRoomCategoryBedService {

    SuccessResponse create(CreateResortRoomCategoryBedRequest request,
                           ResortRoomCategoryEntity resortRoomCategoryEntity,
                           BedTypeEntity bedTypeEntity);

    ResortRoomCategoryBedEntity getEntityById(Long resortRoomCategoryId, Long id);

    ResortRoomCategoryBedResponse getById(Long resortRoomCategoryId, Long id);

    PaginatedResponse<ResortRoomCategoryBedDto> getAll(Long resortRoomCategoryId, ResortRoomCategoryBedFilterRequest request);

    SuccessResponse update(ResortRoomCategoryBedEntity entity, UpdateResortRoomCategoryBedRequest request);

    SuccessResponse delete(ResortRoomCategoryBedEntity entity);
}
