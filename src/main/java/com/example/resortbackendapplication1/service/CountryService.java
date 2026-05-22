package com.example.resortbackendapplication1.service;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.dto.request.country.CreateCountryRequest;
import com.example.resortbackendapplication1.dto.request.country.UpdateCountryRequest;
import com.example.resortbackendapplication1.dto.response.countries.CountryResponse;
import com.example.resortbackendapplication1.model.entity.CountryEntity;
import com.example.resortbackendapplication1.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.model.projection.CountrySummary;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface CountryService {
    SuccessResponse create(CreateCountryRequest request,
                           Map<Long, LocaleEntity> localeEntityMap);

    CountryEntity getEntityById(Long id);

    CountryResponse getById(Long id);

    PaginatedResponse<CountrySummary> getAll(PaginatedRequest request);

    SuccessResponse update(CountryEntity entity,
                           UpdateCountryRequest request);

    SuccessResponse delete(Long id);

    List<CountryEntity> getAll(Set<Long> ids);
}
