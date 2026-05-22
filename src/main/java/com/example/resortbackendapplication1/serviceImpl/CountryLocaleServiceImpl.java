package com.example.resortbackendapplication1.serviceImpl;

import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.dto.request.country.countrylocale.CreateCountryLocaleRequest;
import com.example.resortbackendapplication1.dto.request.country.countrylocale.UpdateCountryLocaleRequest;
import com.example.resortbackendapplication1.model.entity.CountryEntity;
import com.example.resortbackendapplication1.model.entity.CountryLocaleEntity;
import com.example.resortbackendapplication1.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.model.mapper.CountryLocaleMapper;
import com.example.resortbackendapplication1.repository.CountryLocaleRepository;
import com.example.resortbackendapplication1.service.CountryLocaleService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class CountryLocaleServiceImpl implements CountryLocaleService {
    private final CountryLocaleRepository countryLocaleRepository;

    public CountryLocaleServiceImpl(CountryLocaleRepository countryLocaleRepository) {
        this.countryLocaleRepository = countryLocaleRepository;
    }

    @Transactional
    @Override
    public SuccessResponse create(CountryEntity countryEntity,
                                  LocaleEntity localeEntity,
                                  CreateCountryLocaleRequest request) {
        CountryLocaleEntity entity = CountryLocaleMapper.create(request, countryEntity, localeEntity);
        countryLocaleRepository.save(entity);
        log.info("CountryLocale created with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse update(CountryLocaleEntity entity,
                                  UpdateCountryLocaleRequest request) {
        CountryLocaleMapper.update(entity, request);
        countryLocaleRepository.save(entity);
        log.info("CountryLocale updated with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse delete(CountryLocaleEntity entity) {
        entity.setIsDeleted(true);
        entity.setIsActive(false);
        countryLocaleRepository.save(entity);
        log.info("CountryLocale soft-deleted with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public CountryLocaleEntity getEntityById(Long countryId, Long id) {
        return countryLocaleRepository
                .findByCountryEntity_IdAndIdAndIsActiveAndIsDeleted(countryId, id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("CountryLocale not found with id: " + id));
    }
}
