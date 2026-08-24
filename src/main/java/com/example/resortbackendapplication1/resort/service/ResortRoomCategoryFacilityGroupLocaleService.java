package com.example.resortbackendapplication1.resort.service;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.resort.dto.request.resortroomcategoryfacilitygroup.locale.CreateResortRoomCategoryFacilityGroupLocaleRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortroomcategoryfacilitygroup.locale.UpdateResortRoomCategoryFacilityGroupLocaleRequest;
import com.example.resortbackendapplication1.resort.dto.response.resortroomcategoryfacilitygrouplocales.ResortRoomCategoryFacilityGroupLocaleCountResponse;
import com.example.resortbackendapplication1.resort.model.dto.ResortRoomCategoryFacilityGroupLocaleDto;
import com.example.resortbackendapplication1.resort.model.entity.ResortRoomCategoryFacilityGroupEntity;
import com.example.resortbackendapplication1.resort.model.entity.ResortRoomCategoryFacilityGroupLocaleEntity;

public interface ResortRoomCategoryFacilityGroupLocaleService {

    SuccessResponse create(CreateResortRoomCategoryFacilityGroupLocaleRequest request,
                           ResortRoomCategoryFacilityGroupEntity resortRoomCategoryFacilityGroupEntity,
                           LocaleEntity localeEntity);

    ResortRoomCategoryFacilityGroupLocaleEntity getEntityById(Long resortRoomCategoryFacilityGroupId, Long id);

    PaginatedResponse<ResortRoomCategoryFacilityGroupLocaleDto> getAll(Long resortRoomCategoryFacilityGroupId, String localeCode, PaginatedRequest paginatedRequest);

    ResortRoomCategoryFacilityGroupLocaleCountResponse getActiveCount(Long resortRoomCategoryFacilityGroupId);

    SuccessResponse update(ResortRoomCategoryFacilityGroupLocaleEntity entity,
                           UpdateResortRoomCategoryFacilityGroupLocaleRequest request);

    SuccessResponse delete(ResortRoomCategoryFacilityGroupLocaleEntity entity);
}
