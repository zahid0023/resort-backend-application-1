package com.example.resortbackendapplication1.service;

import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.dto.request.facilitygroups.facilitygrouplocale.CreateFacilityGroupLocaleRequest;
import com.example.resortbackendapplication1.dto.request.facilitygroups.facilitygrouplocale.UpdateFacilityGroupLocaleRequest;
import com.example.resortbackendapplication1.model.entity.FacilityGroupEntity;
import com.example.resortbackendapplication1.model.entity.FacilityGroupLocaleEntity;
import com.example.resortbackendapplication1.model.entity.LocaleEntity;

public interface FacilityGroupLocaleService {
    SuccessResponse create(FacilityGroupEntity facilityGroupEntity,
                           LocaleEntity localeEntity,
                           CreateFacilityGroupLocaleRequest request);

    FacilityGroupLocaleEntity getEntityById(Long facilityGroupId, Long id);

    SuccessResponse update(FacilityGroupLocaleEntity entity,
                           UpdateFacilityGroupLocaleRequest request);

    SuccessResponse delete(FacilityGroupLocaleEntity entity);
}
