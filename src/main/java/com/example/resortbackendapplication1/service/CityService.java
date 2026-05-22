package com.example.resortbackendapplication1.service;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.dto.request.city.CreateCityRequest;
import com.example.resortbackendapplication1.dto.request.city.UpdateCityRequest;
import com.example.resortbackendapplication1.dto.response.cities.CityResponse;
import com.example.resortbackendapplication1.model.entity.CityEntity;
import com.example.resortbackendapplication1.model.entity.CountryEntity;
import com.example.resortbackendapplication1.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.model.projection.CitySummary;

import java.util.Map;

public interface CityService {
    SuccessResponse create(CreateCityRequest request,
                           CountryEntity countryEntity,
                           Map<Long, LocaleEntity> localeEntityMap);

    CityEntity getEntityById(Long countryId, Long id);

    CityResponse getById(Long countryId, Long id);

    PaginatedResponse<CitySummary> getAll(Long countryId, PaginatedRequest request);

    SuccessResponse update(CityEntity entity,
                           UpdateCityRequest request);

    SuccessResponse delete(CityEntity entity);
}
