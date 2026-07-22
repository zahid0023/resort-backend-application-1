package com.example.resortbackendapplication1.resortfacilityprice.service;

import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.currency.model.entity.CurrencyEntity;
import com.example.resortbackendapplication1.price.model.entity.PriceUnitEntity;
import com.example.resortbackendapplication1.resort.model.entity.ResortFacilityEntity;
import com.example.resortbackendapplication1.resortfacilityprice.dto.request.CreateResortFacilityPriceRequest;
import com.example.resortbackendapplication1.resortfacilityprice.dto.request.ResortFacilityPriceFilterRequest;
import com.example.resortbackendapplication1.resortfacilityprice.dto.request.UpdateResortFacilityPriceRequest;
import com.example.resortbackendapplication1.resortfacilityprice.dto.response.ResortFacilityPriceResponse;
import com.example.resortbackendapplication1.resortfacilityprice.model.dto.ResortFacilityPriceDto;
import com.example.resortbackendapplication1.resortfacilityprice.model.entity.ResortFacilityPriceEntity;

public interface ResortFacilityPriceService {

    SuccessResponse create(CreateResortFacilityPriceRequest request,
                           ResortFacilityEntity resortFacilityEntity,
                           PriceUnitEntity priceUnitEntity,
                           CurrencyEntity currencyEntity);

    ResortFacilityPriceEntity getEntityById(Long resortFacilityId, Long id);

    ResortFacilityPriceResponse getById(Long resortFacilityId, Long id);

    PaginatedResponse<ResortFacilityPriceDto> getAll(ResortFacilityPriceFilterRequest request,
                                                      Long resortFacilityId);

    SuccessResponse update(ResortFacilityPriceEntity entity,
                           UpdateResortFacilityPriceRequest request,
                           PriceUnitEntity priceUnitEntity);

    SuccessResponse delete(ResortFacilityPriceEntity entity);
}
