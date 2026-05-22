package com.example.resortbackendapplication1.service;

import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.dto.request.country.countrylocale.CreateCountryLocaleRequest;
import com.example.resortbackendapplication1.dto.request.country.countrylocale.UpdateCountryLocaleRequest;
import com.example.resortbackendapplication1.model.entity.CountryEntity;
import com.example.resortbackendapplication1.model.entity.CountryLocaleEntity;
import com.example.resortbackendapplication1.model.entity.LocaleEntity;

public interface CountryLocaleService {
    SuccessResponse create(CountryEntity countryEntity,
                           LocaleEntity localeEntity,
                           CreateCountryLocaleRequest request);

    CountryLocaleEntity getEntityById(Long countryId, Long id);

    SuccessResponse update(CountryLocaleEntity entity,
                           UpdateCountryLocaleRequest request);

    SuccessResponse delete(CountryLocaleEntity entity);
}
