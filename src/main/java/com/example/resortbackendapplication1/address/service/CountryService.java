package com.example.resortbackendapplication1.address.service;

import com.example.resortbackendapplication1.address.dto.request.country.CountryFilterRequest;
import com.example.resortbackendapplication1.address.dto.request.country.CreateCountryRequest;
import com.example.resortbackendapplication1.address.dto.request.country.UpdateCountryRequest;
import com.example.resortbackendapplication1.address.dto.response.countries.CountryResponse;
import com.example.resortbackendapplication1.address.model.dto.CountryDto;
import com.example.resortbackendapplication1.address.model.entity.CountryEntity;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;

public interface CountryService {

    SuccessResponse create(CreateCountryRequest request,
                           LocaleEntity localeEntity);

    CountryEntity getEntityById(Long id);

    CountryResponse getById(Long id);

    PaginatedResponse<CountryDto> getAll(CountryFilterRequest request);

    SuccessResponse update(CountryEntity entity,
                           UpdateCountryRequest request);

    SuccessResponse delete(CountryEntity entity);

    SuccessResponse updateFlagImage(CountryEntity entity, String flagUrl);
}
