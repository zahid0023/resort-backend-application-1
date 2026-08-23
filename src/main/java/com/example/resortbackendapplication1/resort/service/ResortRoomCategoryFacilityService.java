package com.example.resortbackendapplication1.resort.service;

import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.facility.model.entity.FacilityEntity;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.resort.dto.request.resortroomcategoryfacility.CreateResortRoomCategoryFacilityRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortroomcategoryfacility.ResortRoomCategoryFacilityFilterRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortroomcategoryfacility.UpdateResortRoomCategoryFacilityRequest;
import com.example.resortbackendapplication1.resort.dto.response.resortroomcategoryfacilities.ResortRoomCategoryFacilityResponse;
import com.example.resortbackendapplication1.resort.model.dto.ResortRoomCategoryFacilityDto;
import com.example.resortbackendapplication1.resort.model.entity.ResortRoomCategoryEntity;
import com.example.resortbackendapplication1.resort.model.entity.ResortRoomCategoryFacilityEntity;
import com.example.resortbackendapplication1.resort.model.entity.ResortRoomCategoryFacilityGroupEntity;

public interface ResortRoomCategoryFacilityService {

    SuccessResponse create(CreateResortRoomCategoryFacilityRequest request,
                           ResortRoomCategoryEntity resortRoomCategoryEntity,
                           ResortRoomCategoryFacilityGroupEntity resortRoomCategoryFacilityGroupEntity,
                           FacilityEntity facilityEntity,
                           LocaleEntity localeEntity);

    ResortRoomCategoryFacilityEntity getEntityById(Long resortRoomCategoryId, Long id);

    ResortRoomCategoryFacilityResponse getById(Long resortRoomCategoryId, Long id);

    PaginatedResponse<ResortRoomCategoryFacilityDto> getAll(Long resortRoomCategoryId, ResortRoomCategoryFacilityFilterRequest request);

    SuccessResponse update(ResortRoomCategoryFacilityEntity entity,
                           UpdateResortRoomCategoryFacilityRequest request);

    SuccessResponse delete(ResortRoomCategoryFacilityEntity entity);
}
