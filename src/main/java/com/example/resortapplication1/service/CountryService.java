package com.example.resortapplication1.service;

import com.example.resortapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortapplication1.commons.dto.response.SuccessResponse;
import com.example.resortapplication1.dto.request.countries.CreateCountryRequest;
import com.example.resortapplication1.dto.request.countries.UpdateCountryRequest;
import com.example.resortapplication1.dto.response.countries.CountryResponse;
import com.example.resortapplication1.model.dto.CountryDto;
import com.example.resortapplication1.model.entity.CountryEntity;
import org.springframework.data.domain.Pageable;

public interface CountryService {
    SuccessResponse createCountry(CreateCountryRequest request);

    CountryEntity getCountryEntity(Long id);

    CountryResponse getCountry(Long id);

    PaginatedResponse<CountryDto> getAllCountries(Pageable pageable);

    SuccessResponse updateCountry(Long id, UpdateCountryRequest request);

    SuccessResponse deleteCountry(Long id);
}
