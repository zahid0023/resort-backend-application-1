package com.example.resortbackendapplication1.serviceImpl;

import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.dto.request.city.citylocale.CreateCityLocaleRequest;
import com.example.resortbackendapplication1.dto.request.city.citylocale.UpdateCityLocaleRequest;
import com.example.resortbackendapplication1.model.entity.CityEntity;
import com.example.resortbackendapplication1.model.entity.CityLocaleEntity;
import com.example.resortbackendapplication1.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.model.mapper.CityLocaleMapper;
import com.example.resortbackendapplication1.repository.CityLocaleRepository;
import com.example.resortbackendapplication1.service.CityLocaleService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class CityLocaleServiceImpl implements CityLocaleService {

    private final CityLocaleRepository cityLocaleRepository;

    public CityLocaleServiceImpl(CityLocaleRepository cityLocaleRepository) {
        this.cityLocaleRepository = cityLocaleRepository;
    }

    @Transactional
    @Override
    public SuccessResponse create(CityEntity cityEntity,
                                  LocaleEntity localeEntity,
                                  CreateCityLocaleRequest request) {
        CityLocaleEntity entity = CityLocaleMapper.create(request, cityEntity, localeEntity);
        cityLocaleRepository.save(entity);
        log.info("CityLocale created with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse update(CityLocaleEntity entity,
                                  UpdateCityLocaleRequest request) {
        CityLocaleMapper.update(entity, request);
        cityLocaleRepository.save(entity);
        log.info("CityLocale updated with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse delete(CityLocaleEntity entity) {
        entity.setIsDeleted(true);
        entity.setIsActive(false);
        cityLocaleRepository.save(entity);
        log.info("CityLocale soft-deleted with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public CityLocaleEntity getEntityById(Long countryId, Long cityId, Long id) {
        return cityLocaleRepository
                .findByCityEntity_IdAndIdAndIsActiveAndIsDeleted(cityId, id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("CityLocale not found with id: " + id));
    }
}
