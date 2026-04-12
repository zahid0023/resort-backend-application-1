package com.example.resortbackendapplication1.service;

import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.dto.request.cities.CreateCityRequest;
import com.example.resortbackendapplication1.dto.request.cities.UpdateCityRequest;
import com.example.resortbackendapplication1.dto.response.cities.CityResponse;
import com.example.resortbackendapplication1.model.dto.CityDto;
import com.example.resortbackendapplication1.model.entity.CityEntity;
import org.springframework.data.domain.Pageable;

public interface CityService {
    SuccessResponse createCity(CreateCityRequest request);

    CityEntity getCityEntity(Long id);

    CityResponse getCity(Long id);

    PaginatedResponse<CityDto> getAllCities(Pageable pageable);

    SuccessResponse updateCity(Long id, UpdateCityRequest request);

    SuccessResponse deleteCity(Long id);
}
