package com.example.resortbackendapplication1.price.service;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.price.dto.request.resortfacilitypricetype.locale.CreateFacilityPriceTypeLocaleRequest;
import com.example.resortbackendapplication1.price.dto.request.resortfacilitypricetype.locale.UpdateFacilityPriceTypeLocaleRequest;
import com.example.resortbackendapplication1.price.model.dto.FacilityPriceTypeLocaleDto;
import com.example.resortbackendapplication1.price.model.entity.FacilityPriceTypeEntity;
import com.example.resortbackendapplication1.price.model.entity.FacilityPriceTypeLocaleEntity;
import com.example.resortbackendapplication1.locale.dto.response.locales.LocaleCountResponse;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;

public interface FacilityPriceTypeLocaleService {
    SuccessResponse create(CreateFacilityPriceTypeLocaleRequest request,
                           FacilityPriceTypeEntity facilityPriceTypeEntity,
                           LocaleEntity localeEntity);

    FacilityPriceTypeLocaleEntity getEntityById(Long facilityPriceTypeId, Long id);

    PaginatedResponse<FacilityPriceTypeLocaleDto> getAll(Long facilityPriceTypeId, String localeCode, PaginatedRequest paginatedRequest);

    LocaleCountResponse getCount(Long facilityPriceTypeId);

    SuccessResponse update(FacilityPriceTypeLocaleEntity entity,
                           UpdateFacilityPriceTypeLocaleRequest request);

    SuccessResponse delete(FacilityPriceTypeLocaleEntity entity);
}
