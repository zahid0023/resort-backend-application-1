package com.example.resortbackendapplication1.facility.serviceImpl;

import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.facility.dto.request.facilities.facilitylocale.CreateFacilityLocaleRequest;
import com.example.resortbackendapplication1.facility.dto.request.facilities.facilitylocale.UpdateFacilityLocaleRequest;
import com.example.resortbackendapplication1.facility.model.entity.FacilityEntity;
import com.example.resortbackendapplication1.facility.model.entity.FacilityLocaleEntity;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.facility.model.mapper.FacilityLocaleMapper;
import com.example.resortbackendapplication1.facility.repository.FacilityLocaleRepository;
import com.example.resortbackendapplication1.facility.service.FacilityLocaleService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class FacilityLocaleServiceImpl implements FacilityLocaleService {

    private final FacilityLocaleRepository facilityLocaleRepository;

    public FacilityLocaleServiceImpl(FacilityLocaleRepository facilityLocaleRepository) {
        this.facilityLocaleRepository = facilityLocaleRepository;
    }

    @Transactional
    @Override
    public SuccessResponse create(FacilityEntity facilityEntity,
                                  LocaleEntity localeEntity,
                                  CreateFacilityLocaleRequest request) {
        FacilityLocaleEntity entity = FacilityLocaleMapper.create(request, facilityEntity, localeEntity);
        facilityLocaleRepository.save(entity);
        log.info("FacilityLocale created with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse update(FacilityLocaleEntity entity, UpdateFacilityLocaleRequest request) {
        FacilityLocaleMapper.update(entity, request);
        facilityLocaleRepository.save(entity);
        log.info("FacilityLocale updated with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse delete(FacilityLocaleEntity entity) {
        entity.setIsDeleted(true);
        entity.setIsActive(false);
        facilityLocaleRepository.save(entity);
        log.info("FacilityLocale soft-deleted with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public FacilityLocaleEntity getEntityById(Long facilityId, Long id) {
        return facilityLocaleRepository
                .findByFacilityEntity_IdAndIdAndIsActiveAndIsDeleted(facilityId, id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("FacilityLocale not found with id: " + id));
    }
}
