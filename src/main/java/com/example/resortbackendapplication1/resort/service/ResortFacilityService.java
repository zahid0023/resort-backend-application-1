package com.example.resortbackendapplication1.resort.service;

import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.currency.model.entity.CurrencyEntity;
import com.example.resortbackendapplication1.dayofweek.model.entity.DayOfWeekEntity;
import com.example.resortbackendapplication1.facility.model.entity.FacilityEntity;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.price.model.entity.PriceTypeEntity;
import com.example.resortbackendapplication1.price.model.entity.PriceUnitEntity;
import com.example.resortbackendapplication1.resort.dto.request.resortfacility.CreateResortFacilityRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortfacility.ResortFacilityFilterRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortfacility.UpdateResortFacilityRequest;
import com.example.resortbackendapplication1.resort.dto.response.resortfacilities.ResortFacilityResponse;
import com.example.resortbackendapplication1.resort.model.dto.ResortFacilityDto;
import com.example.resortbackendapplication1.resort.model.entity.ResortEntity;
import com.example.resortbackendapplication1.resort.model.entity.ResortFacilityEntity;
import com.example.resortbackendapplication1.resort.model.entity.ResortFacilityGroupEntity;

import java.util.List;

public interface ResortFacilityService {

    SuccessResponse create(CreateResortFacilityRequest request,
                           ResortEntity resortEntity,
                           ResortFacilityGroupEntity resortFacilityGroupEntity,
                           FacilityEntity facilityEntity,
                           LocaleEntity localeEntity,
                           List<DayOfWeekEntity> allDaysOfWeek,
                           PriceTypeEntity priceTypeEntity,
                           PriceUnitEntity priceUnitEntity,
                           CurrencyEntity currencyEntity);

    ResortFacilityEntity getEntityById(Long resortId, Long id);

    ResortFacilityResponse getById(Long resortId, Long id);

    PaginatedResponse<ResortFacilityDto> getAll(Long resortId, ResortFacilityFilterRequest request);

    SuccessResponse update(ResortFacilityEntity entity,
                           ResortFacilityGroupEntity resortFacilityGroupEntity,
                           UpdateResortFacilityRequest request);

    SuccessResponse delete(ResortFacilityEntity entity);
}
