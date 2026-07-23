package com.example.resortbackendapplication1.resort.service;

import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.currency.model.entity.CurrencyEntity;
import com.example.resortbackendapplication1.facility.model.entity.FacilityEntity;
import com.example.resortbackendapplication1.facilitypricetype.model.entity.FacilityPriceTypeEntity;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.price.model.entity.PriceUnitEntity;
import com.example.resortbackendapplication1.resort.dto.request.resortfacility.CreateResortFacilityRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortfacility.ResortFacilityFilterRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortfacility.SetResortFacilityHighlightsRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortfacility.UpdateResortFacilityRequest;
import com.example.resortbackendapplication1.resort.dto.response.ResortFacilityResponse;
import com.example.resortbackendapplication1.resort.model.dto.ResortFacilityDto;
import com.example.resortbackendapplication1.resort.model.entity.ResortEntity;
import com.example.resortbackendapplication1.resort.model.entity.ResortFacilityEntity;
import com.example.resortbackendapplication1.resort.model.entity.ResortFacilityGroupEntity;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ResortFacilityService {

    SuccessResponse create(CreateResortFacilityRequest request,
                           ResortEntity resortEntity,
                           ResortFacilityGroupEntity resortFacilityGroupEntity,
                           FacilityEntity facilityEntity,
                           FacilityPriceTypeEntity facilityPriceTypeEntity,
                           PriceUnitEntity priceUnitEntity,
                           CurrencyEntity currencyEntity,
                           Map<Long, LocaleEntity> localeEntityMap);

    ResortFacilityEntity getEntityById(Long id);

    ResortFacilityResponse getById(Long id, Long resortId);

    PaginatedResponse<ResortFacilityDto> getAll(ResortFacilityFilterRequest request, Long resortFacilityGroupId);

    SuccessResponse update(ResortFacilityEntity entity,
                           UpdateResortFacilityRequest request,
                           FacilityEntity facilityEntity,
                           FacilityPriceTypeEntity facilityPriceTypeEntity);

    SuccessResponse delete(Long id);

    List<ResortFacilityEntity> getAll(Set<Long> ids);

    SuccessResponse setHighlights(Long resortId, SetResortFacilityHighlightsRequest request);
}
