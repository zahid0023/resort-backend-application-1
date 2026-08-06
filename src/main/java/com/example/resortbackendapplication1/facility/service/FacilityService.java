package com.example.resortbackendapplication1.facility.service;

import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.facility.dto.request.facility.CreateFacilityRequest;
import com.example.resortbackendapplication1.facility.dto.request.facility.FacilityFilterRequest;
import com.example.resortbackendapplication1.facility.dto.request.facility.UpdateFacilityRequest;
import com.example.resortbackendapplication1.facility.dto.response.facilities.FacilityResponse;
import com.example.resortbackendapplication1.facility.model.dto.FacilityDto;
import com.example.resortbackendapplication1.facility.model.entity.FacilityEntity;
import com.example.resortbackendapplication1.facility.model.entity.FacilityGroupEntity;
import com.example.resortbackendapplication1.facility.model.entity.FacilityScopeEntity;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;

import java.util.List;

public interface FacilityService {

    SuccessResponse create(CreateFacilityRequest request,
                           List<FacilityGroupEntity> facilityGroupEntities,
                           List<FacilityScopeEntity> facilityScopeEntities,
                           LocaleEntity localeEntity);

    FacilityEntity getEntityById(Long id);

    FacilityResponse getById(Long id);

    PaginatedResponse<FacilityDto> getAll(FacilityFilterRequest request);

    SuccessResponse update(FacilityEntity entity,
                           UpdateFacilityRequest request);

    SuccessResponse delete(FacilityEntity entity);
}
