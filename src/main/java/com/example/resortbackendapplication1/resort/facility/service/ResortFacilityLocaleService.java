package com.example.resortbackendapplication1.resort.facility.service;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.resort.facility.dto.request.resortfacility.locale.CreateResortFacilityLocaleRequest;
import com.example.resortbackendapplication1.resort.facility.dto.request.resortfacility.locale.UpdateResortFacilityLocaleRequest;
import com.example.resortbackendapplication1.resort.facility.dto.response.resortfacilitylocales.ResortFacilityLocaleCountResponse;
import com.example.resortbackendapplication1.resort.facility.model.dto.ResortFacilityLocaleDto;
import com.example.resortbackendapplication1.resort.facility.model.entity.ResortFacilityEntity;
import com.example.resortbackendapplication1.resort.facility.model.entity.ResortFacilityLocaleEntity;

public interface ResortFacilityLocaleService {

    SuccessResponse create(CreateResortFacilityLocaleRequest request,
                           ResortFacilityEntity resortFacilityEntity,
                           LocaleEntity localeEntity);

    ResortFacilityLocaleEntity getEntityById(Long resortFacilityId, Long id);

    PaginatedResponse<ResortFacilityLocaleDto> getAll(Long resortFacilityId, String localeCode, PaginatedRequest paginatedRequest);

    ResortFacilityLocaleCountResponse getActiveCount(Long resortFacilityId);

    SuccessResponse update(ResortFacilityLocaleEntity entity,
                           UpdateResortFacilityLocaleRequest request);

    SuccessResponse delete(ResortFacilityLocaleEntity entity);
}
