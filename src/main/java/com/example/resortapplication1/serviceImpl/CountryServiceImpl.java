package com.example.resortapplication1.serviceImpl;

import com.example.resortapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortapplication1.commons.dto.response.SuccessResponse;
import com.example.resortapplication1.dto.request.countries.CreateCountryRequest;
import com.example.resortapplication1.dto.request.countries.UpdateCountryRequest;
import com.example.resortapplication1.dto.response.countries.CountryResponse;
import com.example.resortapplication1.model.dto.CountryDto;
import com.example.resortapplication1.model.entity.CountryEntity;
import com.example.resortapplication1.model.mapper.CountryMapper;
import com.example.resortapplication1.model.projection.CountrySummary;
import com.example.resortapplication1.repository.CountryRepository;
import com.example.resortapplication1.service.CountryService;
import com.example.resortapplication1.utils.Pagination;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CountryServiceImpl implements CountryService {

    private final CountryRepository countryRepository;

    public CountryServiceImpl(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @Override
    public SuccessResponse createCountry(CreateCountryRequest request) {
        CountryEntity entity = CountryMapper.fromRequest(request);
        entity = countryRepository.save(entity);
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public CountryResponse getCountry(Long id) {
        CountryEntity entity = getCountryEntity(id);
        return new CountryResponse(CountryMapper.toDto(entity));
    }

    @Override
    public PaginatedResponse<CountryDto> getAllCountries(Pageable pageable) {
        Page<@NonNull CountrySummary> page = countryRepository
                .findAllByIsActiveAndIsDeleted(true, false, pageable, CountrySummary.class);

        Page<@NonNull CountryDto> dtoPage = page.map(p ->
                CountryDto.builder()
                        .id(p.getId())
                        .code(p.getCode())
                        .name(p.getName())
                        .build()
        );
        return Pagination.buildPaginatedResponse(dtoPage);
    }

    @Override
    public SuccessResponse updateCountry(Long id, UpdateCountryRequest request) {
        CountryEntity entity = getCountryEntity(id);
        CountryMapper.updateEntity(entity, request);
        entity = countryRepository.save(entity);
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public SuccessResponse deleteCountry(Long id) {
        CountryEntity entity = getCountryEntity(id);
        entity.setIsDeleted(true);
        entity.setIsActive(false);
        countryRepository.save(entity);
        return new SuccessResponse(true, id);
    }

    @Override
    public CountryEntity getCountryEntity(Long id) {
        return countryRepository.findByIdAndIsActiveAndIsDeleted(id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("Country with id: " + id + " was not found."));
    }
}
