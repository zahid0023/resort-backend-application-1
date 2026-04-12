package com.example.resortbackendapplication1.serviceImpl;

import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.dto.request.cities.CreateCityRequest;
import com.example.resortbackendapplication1.dto.request.cities.UpdateCityRequest;
import com.example.resortbackendapplication1.dto.response.cities.CityResponse;
import com.example.resortbackendapplication1.model.dto.CityDto;
import com.example.resortbackendapplication1.model.entity.CityEntity;
import com.example.resortbackendapplication1.model.entity.CountryEntity;
import com.example.resortbackendapplication1.model.mapper.CityMapper;
import com.example.resortbackendapplication1.model.projection.CitySummary;
import com.example.resortbackendapplication1.repository.CityRepository;
import com.example.resortbackendapplication1.service.CityService;
import com.example.resortbackendapplication1.service.CountryService;
import com.example.resortbackendapplication1.utils.Pagination;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CityServiceImpl implements CityService {

    private final CityRepository cityRepository;
    private final CountryService countryService;

    public CityServiceImpl(CityRepository cityRepository, CountryService countryService) {
        this.cityRepository = cityRepository;
        this.countryService = countryService;
    }

    @Override
    public SuccessResponse createCity(CreateCityRequest request) {
        CountryEntity country = countryService.getCountryEntity(request.getCountryId());
        CityEntity entity = CityMapper.fromRequest(request, country);
        entity = cityRepository.save(entity);
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public CityResponse getCity(Long id) {
        CityEntity entity = getCityEntity(id);
        return new CityResponse(CityMapper.toDto(entity));
    }

    @Override
    public PaginatedResponse<CityDto> getAllCities(Pageable pageable) {
        Page<@NonNull CitySummary> page = cityRepository
                .findAllByIsActiveAndIsDeleted(true, false, pageable, CitySummary.class);

        Page<@NonNull CityDto> dtoPage = page.map(p ->
                CityDto.builder()
                        .id(p.getId())
                        .name(p.getName())
                        .countryId(p.getCountryId())
                        .build()
        );
        return Pagination.buildPaginatedResponse(dtoPage);
    }

    @Override
    public SuccessResponse updateCity(Long id, UpdateCityRequest request) {
        CityEntity entity = getCityEntity(id);
        CountryEntity country = request.getCountryId() != null
                ? countryService.getCountryEntity(request.getCountryId())
                : null;
        CityMapper.updateEntity(entity, request, country);
        entity = cityRepository.save(entity);
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public SuccessResponse deleteCity(Long id) {
        CityEntity entity = getCityEntity(id);
        entity.setIsDeleted(true);
        entity.setIsActive(false);
        cityRepository.save(entity);
        return new SuccessResponse(true, id);
    }

    @Override
    public CityEntity getCityEntity(Long id) {
        return cityRepository.findByIdAndIsActiveAndIsDeleted(id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("City with id: " + id + " was not found."));
    }
}
