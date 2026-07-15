package com.example.resortbackendapplication1.resort.serviceImpl;

import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.resort.dto.request.resortfacility.resortfacilitylocale.CreateResortFacilityLocaleRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortfacility.resortfacilitylocale.UpdateResortFacilityLocaleRequest;
import com.example.resortbackendapplication1.resort.model.entity.ResortFacilityEntity;
import com.example.resortbackendapplication1.resort.model.entity.ResortFacilityLocaleEntity;
import com.example.resortbackendapplication1.resort.model.mapper.ResortFacilityLocaleMapper;
import com.example.resortbackendapplication1.resort.repository.ResortFacilityLocaleRepository;
import com.example.resortbackendapplication1.resort.service.ResortFacilityLocaleService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class ResortFacilityLocaleServiceImpl implements ResortFacilityLocaleService {

    private final ResortFacilityLocaleRepository resortFacilityLocaleRepository;

    public ResortFacilityLocaleServiceImpl(ResortFacilityLocaleRepository resortFacilityLocaleRepository) {
        this.resortFacilityLocaleRepository = resortFacilityLocaleRepository;
    }

    @Transactional
    @Override
    public SuccessResponse create(ResortFacilityEntity resortFacilityEntity,
                                  LocaleEntity localeEntity,
                                  CreateResortFacilityLocaleRequest request) {
        ResortFacilityLocaleEntity entity = ResortFacilityLocaleMapper.create(request, resortFacilityEntity, localeEntity);
        resortFacilityLocaleRepository.save(entity);
        log.info("ResortFacilityLocale created with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public ResortFacilityLocaleEntity getEntityById(Long resortFacilityId, Long id) {
        return resortFacilityLocaleRepository
                .findByResortFacilityEntity_IdAndIdAndIsActiveAndIsDeleted(resortFacilityId, id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("ResortFacilityLocale not found with id: " + id));
    }

    @Transactional
    @Override
    public SuccessResponse update(ResortFacilityLocaleEntity entity,
                                  UpdateResortFacilityLocaleRequest request) {
        ResortFacilityLocaleMapper.update(entity, request);
        resortFacilityLocaleRepository.save(entity);
        log.info("ResortFacilityLocale updated with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse delete(ResortFacilityLocaleEntity entity) {
        entity.setIsDeleted(true);
        entity.setIsActive(false);
        resortFacilityLocaleRepository.save(entity);
        log.info("ResortFacilityLocale soft-deleted with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }
}
