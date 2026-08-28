package com.example.resortbackendapplication1.price.service;

import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.price.dto.request.resortfacilitypricetype.CreateFacilityPriceTypeRequest;
import com.example.resortbackendapplication1.price.dto.request.resortfacilitypricetype.FacilityPriceTypeFilterRequest;
import com.example.resortbackendapplication1.price.dto.request.resortfacilitypricetype.UpdateFacilityPriceTypeRequest;
import com.example.resortbackendapplication1.price.dto.response.resortfacilitypricetypes.FacilityPriceTypeResponse;
import com.example.resortbackendapplication1.price.model.dto.FacilityPriceTypeDto;
import com.example.resortbackendapplication1.price.model.entity.FacilityPriceTypeEntity;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;

public interface FacilityPriceTypeService {

    SuccessResponse create(CreateFacilityPriceTypeRequest request,
                           LocaleEntity localeEntity);

    FacilityPriceTypeEntity getEntityById(Long id);

    FacilityPriceTypeEntity getEntityByCode(String code);

    FacilityPriceTypeResponse getById(Long id);

    PaginatedResponse<FacilityPriceTypeDto> getAll(FacilityPriceTypeFilterRequest request);

    SuccessResponse update(FacilityPriceTypeEntity entity,
                           UpdateFacilityPriceTypeRequest request);

    SuccessResponse delete(FacilityPriceTypeEntity entity);
}
