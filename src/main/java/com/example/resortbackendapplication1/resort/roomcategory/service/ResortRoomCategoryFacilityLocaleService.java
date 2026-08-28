package com.example.resortbackendapplication1.resort.roomcategory.service;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.resort.roomcategory.dto.request.resortroomcategoryfacility.locale.CreateResortRoomCategoryFacilityLocaleRequest;
import com.example.resortbackendapplication1.resort.roomcategory.dto.request.resortroomcategoryfacility.locale.UpdateResortRoomCategoryFacilityLocaleRequest;
import com.example.resortbackendapplication1.resort.roomcategory.dto.response.resortroomcategoryfacilitylocales.ResortRoomCategoryFacilityLocaleCountResponse;
import com.example.resortbackendapplication1.resort.roomcategory.model.dto.ResortRoomCategoryFacilityLocaleDto;
import com.example.resortbackendapplication1.resort.roomcategory.model.entity.ResortRoomCategoryFacilityEntity;
import com.example.resortbackendapplication1.resort.roomcategory.model.entity.ResortRoomCategoryFacilityLocaleEntity;

public interface ResortRoomCategoryFacilityLocaleService {

    SuccessResponse create(CreateResortRoomCategoryFacilityLocaleRequest request,
                           ResortRoomCategoryFacilityEntity resortRoomCategoryFacilityEntity,
                           LocaleEntity localeEntity);

    ResortRoomCategoryFacilityLocaleEntity getEntityById(Long resortRoomCategoryFacilityId, Long id);

    PaginatedResponse<ResortRoomCategoryFacilityLocaleDto> getAll(Long resortRoomCategoryFacilityId, String localeCode, PaginatedRequest paginatedRequest);

    ResortRoomCategoryFacilityLocaleCountResponse getActiveCount(Long resortRoomCategoryFacilityId);

    SuccessResponse update(ResortRoomCategoryFacilityLocaleEntity entity,
                           UpdateResortRoomCategoryFacilityLocaleRequest request);

    SuccessResponse delete(ResortRoomCategoryFacilityLocaleEntity entity);
}
