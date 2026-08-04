package com.example.resortbackendapplication1.currency.service;

import com.example.resortbackendapplication1.address.model.entity.CountryEntity;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.currency.dto.request.currency.CreateCurrencyRequest;
import com.example.resortbackendapplication1.currency.dto.request.currency.CurrencyFilterRequest;
import com.example.resortbackendapplication1.currency.dto.request.currency.UpdateCurrencyRequest;
import com.example.resortbackendapplication1.currency.dto.response.currencies.CurrencyResponse;
import com.example.resortbackendapplication1.currency.model.dto.CurrencyDto;
import com.example.resortbackendapplication1.currency.model.entity.CurrencyEntity;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;

public interface CurrencyService {

    SuccessResponse create(CreateCurrencyRequest request,
                           CountryEntity countryEntity,
                           LocaleEntity localeEntity);

    CurrencyEntity getEntityById(Long id);

    CurrencyResponse getById(Long id);

    PaginatedResponse<CurrencyDto> getAll(CurrencyFilterRequest request);

    SuccessResponse update(CurrencyEntity entity,
                           UpdateCurrencyRequest request);

    SuccessResponse delete(CurrencyEntity entity);
}
