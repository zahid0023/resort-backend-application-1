package com.example.resortbackendapplication1.resort.service;

import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.resort.dto.request.resortfacilitygroup.resortfacilitygrouplocale.CreateResortFacilityGroupLocaleRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortfacilitygroup.resortfacilitygrouplocale.UpdateResortFacilityGroupLocaleRequest;
import com.example.resortbackendapplication1.resort.model.entity.ResortFacilityGroupEntity;
import com.example.resortbackendapplication1.resort.model.entity.ResortFacilityGroupLocaleEntity;

public interface ResortFacilityGroupLocaleService {

    SuccessResponse create(ResortFacilityGroupEntity resortFacilityGroupEntity,
                           LocaleEntity localeEntity,
                           CreateResortFacilityGroupLocaleRequest request);

    ResortFacilityGroupLocaleEntity getEntityById(Long resortFacilityGroupId, Long id);

    SuccessResponse update(ResortFacilityGroupLocaleEntity entity,
                           UpdateResortFacilityGroupLocaleRequest request);

    SuccessResponse delete(ResortFacilityGroupLocaleEntity entity);
}
