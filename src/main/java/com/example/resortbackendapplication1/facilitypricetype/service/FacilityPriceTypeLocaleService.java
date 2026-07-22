package com.example.resortbackendapplication1.facilitypricetype.service;

import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.facilitypricetype.dto.request.facilitypricetypelocale.CreateFacilityPriceTypeLocaleRequest;
import com.example.resortbackendapplication1.facilitypricetype.dto.request.facilitypricetypelocale.UpdateFacilityPriceTypeLocaleRequest;
import com.example.resortbackendapplication1.facilitypricetype.model.entity.FacilityPriceTypeEntity;
import com.example.resortbackendapplication1.facilitypricetype.model.entity.FacilityPriceTypeLocaleEntity;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;

public interface FacilityPriceTypeLocaleService {

    SuccessResponse create(FacilityPriceTypeEntity facilityPriceTypeEntity,
                           LocaleEntity localeEntity,
                           CreateFacilityPriceTypeLocaleRequest request);

    FacilityPriceTypeLocaleEntity getEntityById(Long facilityPriceTypeId, Long id);

    SuccessResponse update(FacilityPriceTypeLocaleEntity entity, UpdateFacilityPriceTypeLocaleRequest request);

    SuccessResponse delete(FacilityPriceTypeLocaleEntity entity);
}
