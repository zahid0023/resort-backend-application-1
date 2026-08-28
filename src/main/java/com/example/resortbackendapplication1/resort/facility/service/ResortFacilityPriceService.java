package com.example.resortbackendapplication1.resort.facility.service;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.currency.model.entity.CurrencyEntity;
import com.example.resortbackendapplication1.price.model.entity.PriceUnitEntity;
import com.example.resortbackendapplication1.price.model.entity.FacilityPriceTypeEntity;
import com.example.resortbackendapplication1.resort.facility.dto.request.resortfacilityprice.CreateResortFacilityPriceRequest;
import com.example.resortbackendapplication1.resort.facility.dto.request.resortfacilityprice.UpdateResortFacilityPriceRequest;
import com.example.resortbackendapplication1.resort.facility.dto.response.resortfacilityprices.ResortFacilityPriceResponse;
import com.example.resortbackendapplication1.resort.facility.model.dto.ResortFacilityPriceDto;
import com.example.resortbackendapplication1.resort.facility.model.entity.ResortFacilityEntity;
import com.example.resortbackendapplication1.resort.facility.model.entity.ResortFacilityPriceEntity;

public interface ResortFacilityPriceService {

    SuccessResponse create(CreateResortFacilityPriceRequest request,
                           ResortFacilityEntity resortFacilityEntity,
                           FacilityPriceTypeEntity facilityPriceTypeEntity,
                           PriceUnitEntity priceUnitEntity,
                           CurrencyEntity currencyEntity);

    ResortFacilityPriceEntity getEntityById(Long resortFacilityId, Long id);

    ResortFacilityPriceResponse getById(Long resortFacilityId, Long id);

    PaginatedResponse<ResortFacilityPriceDto> getAll(Long resortFacilityId, PaginatedRequest paginatedRequest);

    SuccessResponse update(ResortFacilityPriceEntity entity,
                           UpdateResortFacilityPriceRequest request);

    SuccessResponse delete(ResortFacilityPriceEntity entity);
}
