package com.example.resortbackendapplication1.facilitypricetype.service;

import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.facilitypricetype.dto.request.CreateFacilityPriceTypeRequest;
import com.example.resortbackendapplication1.facilitypricetype.dto.request.FacilityPriceTypeFilterRequest;
import com.example.resortbackendapplication1.facilitypricetype.dto.request.UpdateFacilityPriceTypeRequest;
import com.example.resortbackendapplication1.facilitypricetype.dto.response.FacilityPriceTypeResponse;
import com.example.resortbackendapplication1.facilitypricetype.model.dto.FacilityPriceTypeDto;
import com.example.resortbackendapplication1.facilitypricetype.model.entity.FacilityPriceTypeEntity;

public interface FacilityPriceTypeService {

    SuccessResponse create(CreateFacilityPriceTypeRequest request);

    FacilityPriceTypeEntity getEntityById(Long id);

    FacilityPriceTypeResponse getById(Long id);

    PaginatedResponse<FacilityPriceTypeDto> getAll(FacilityPriceTypeFilterRequest request);

    SuccessResponse update(FacilityPriceTypeEntity entity, UpdateFacilityPriceTypeRequest request);

    SuccessResponse delete(Long id);
}
