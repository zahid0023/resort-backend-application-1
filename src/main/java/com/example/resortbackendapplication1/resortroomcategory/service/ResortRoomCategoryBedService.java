package com.example.resortbackendapplication1.resortroomcategory.service;

import com.example.resortbackendapplication1.bedtype.model.entity.BedTypeEntity;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.resortroomcategory.dto.request.bed.ResortRoomCategoryBedRequest;
import com.example.resortbackendapplication1.resortroomcategory.dto.request.bed.UpdateResortRoomCategoryBedRequest;
import com.example.resortbackendapplication1.resortroomcategory.model.entity.ResortRoomCategoryBedEntity;
import com.example.resortbackendapplication1.resortroomcategory.model.entity.ResortRoomCategoryEntity;

public interface ResortRoomCategoryBedService {

    SuccessResponse create(ResortRoomCategoryEntity resortRoomCategoryEntity,
                           BedTypeEntity bedTypeEntity,
                           ResortRoomCategoryBedRequest request);

    ResortRoomCategoryBedEntity getEntityById(Long resortRoomCategoryId, Long id);

    SuccessResponse update(ResortRoomCategoryBedEntity entity,
                           UpdateResortRoomCategoryBedRequest request);

    SuccessResponse delete(ResortRoomCategoryBedEntity entity);
}
