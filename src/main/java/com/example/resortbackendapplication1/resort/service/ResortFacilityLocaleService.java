package com.example.resortbackendapplication1.resort.service;

import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.resort.dto.request.resortfacility.resortfacilitylocale.CreateResortFacilityLocaleRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortfacility.resortfacilitylocale.UpdateResortFacilityLocaleRequest;
import com.example.resortbackendapplication1.resort.model.entity.ResortFacilityEntity;
import com.example.resortbackendapplication1.resort.model.entity.ResortFacilityLocaleEntity;

public interface ResortFacilityLocaleService {

    SuccessResponse create(ResortFacilityEntity resortFacilityEntity,
                           LocaleEntity localeEntity,
                           CreateResortFacilityLocaleRequest request);

    ResortFacilityLocaleEntity getEntityById(Long resortFacilityId, Long id);

    SuccessResponse update(ResortFacilityLocaleEntity entity,
                           UpdateResortFacilityLocaleRequest request);

    SuccessResponse delete(ResortFacilityLocaleEntity entity);
}
