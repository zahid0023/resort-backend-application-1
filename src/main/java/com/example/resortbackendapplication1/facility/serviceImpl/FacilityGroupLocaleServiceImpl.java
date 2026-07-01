package com.example.resortbackendapplication1.facility.serviceImpl;

import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.facility.dto.request.facilitygroups.facilitygrouplocale.CreateFacilityGroupLocaleRequest;
import com.example.resortbackendapplication1.facility.dto.request.facilitygroups.facilitygrouplocale.UpdateFacilityGroupLocaleRequest;
import com.example.resortbackendapplication1.facility.model.entity.FacilityGroupEntity;
import com.example.resortbackendapplication1.facility.model.entity.FacilityGroupLocaleEntity;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.facility.model.mapper.FacilityGroupLocaleMapper;
import com.example.resortbackendapplication1.facility.repository.FacilityGroupLocaleRepository;
import com.example.resortbackendapplication1.facility.service.FacilityGroupLocaleService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class FacilityGroupLocaleServiceImpl implements FacilityGroupLocaleService {

    private final FacilityGroupLocaleRepository facilityGroupLocaleRepository;

    public FacilityGroupLocaleServiceImpl(FacilityGroupLocaleRepository facilityGroupLocaleRepository) {
        this.facilityGroupLocaleRepository = facilityGroupLocaleRepository;
    }

    @Transactional
    @Override
    public SuccessResponse create(FacilityGroupEntity facilityGroupEntity,
                                  LocaleEntity localeEntity,
                                  CreateFacilityGroupLocaleRequest request) {
        FacilityGroupLocaleEntity entity = FacilityGroupLocaleMapper.create(request, facilityGroupEntity, localeEntity);
        facilityGroupLocaleRepository.save(entity);
        log.info("FacilityGroupLocale created with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse update(FacilityGroupLocaleEntity entity,
                                  UpdateFacilityGroupLocaleRequest request) {
        FacilityGroupLocaleMapper.update(entity, request);
        facilityGroupLocaleRepository.save(entity);
        log.info("FacilityGroupLocale updated with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse delete(FacilityGroupLocaleEntity entity) {
        entity.setIsDeleted(true);
        entity.setIsActive(false);
        facilityGroupLocaleRepository.save(entity);
        log.info("FacilityGroupLocale soft-deleted with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public FacilityGroupLocaleEntity getEntityById(Long facilityGroupId, Long id) {
        return facilityGroupLocaleRepository
                .findByFacilityGroupEntity_IdAndIdAndIsActiveAndIsDeleted(facilityGroupId, id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("FacilityGroupLocale not found with id: " + id));
    }
}
