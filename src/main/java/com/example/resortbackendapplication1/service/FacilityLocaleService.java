package com.example.resortbackendapplication1.service;

import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.dto.request.facilities.facilitylocale.CreateFacilityLocaleRequest;
import com.example.resortbackendapplication1.dto.request.facilities.facilitylocale.UpdateFacilityLocaleRequest;
import com.example.resortbackendapplication1.model.entity.FacilityEntity;
import com.example.resortbackendapplication1.model.entity.FacilityLocaleEntity;
import com.example.resortbackendapplication1.model.entity.LocaleEntity;

public interface FacilityLocaleService {
    SuccessResponse create(FacilityEntity facilityEntity,
                           LocaleEntity localeEntity,
                           CreateFacilityLocaleRequest request);

    FacilityLocaleEntity getEntityById(Long facilityId, Long id);

    SuccessResponse update(FacilityLocaleEntity entity,
                           UpdateFacilityLocaleRequest request);

    SuccessResponse delete(FacilityLocaleEntity entity);
}
