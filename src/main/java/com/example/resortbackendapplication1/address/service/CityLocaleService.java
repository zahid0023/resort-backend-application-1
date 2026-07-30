package com.example.resortbackendapplication1.address.service;

import com.example.resortbackendapplication1.address.dto.request.city.locale.CreateCityLocaleRequest;
import com.example.resortbackendapplication1.address.dto.request.city.locale.UpdateCityLocaleRequest;
import com.example.resortbackendapplication1.address.model.entity.CityEntity;
import com.example.resortbackendapplication1.address.model.entity.CityLocaleEntity;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;

public interface CityLocaleService {
    SuccessResponse create(CreateCityLocaleRequest request,
                           CityEntity cityEntity,
                           LocaleEntity localeEntity);

    CityLocaleEntity getEntityById(Long cityId, Long id);

    SuccessResponse update(CityLocaleEntity entity,
                           UpdateCityLocaleRequest request);

    SuccessResponse delete(CityLocaleEntity entity);
}
