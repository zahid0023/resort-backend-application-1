package com.example.resortbackendapplication1.facilitypricetype.serviceImpl;

import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.facilitypricetype.dto.request.facilitypricetypelocale.CreateFacilityPriceTypeLocaleRequest;
import com.example.resortbackendapplication1.facilitypricetype.dto.request.facilitypricetypelocale.UpdateFacilityPriceTypeLocaleRequest;
import com.example.resortbackendapplication1.facilitypricetype.model.entity.FacilityPriceTypeEntity;
import com.example.resortbackendapplication1.facilitypricetype.model.entity.FacilityPriceTypeLocaleEntity;
import com.example.resortbackendapplication1.facilitypricetype.model.mapper.FacilityPriceTypeLocaleMapper;
import com.example.resortbackendapplication1.facilitypricetype.repository.FacilityPriceTypeLocaleRepository;
import com.example.resortbackendapplication1.facilitypricetype.service.FacilityPriceTypeLocaleService;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class FacilityPriceTypeLocaleServiceImpl implements FacilityPriceTypeLocaleService {

    private final FacilityPriceTypeLocaleRepository facilityPriceTypeLocaleRepository;

    public FacilityPriceTypeLocaleServiceImpl(FacilityPriceTypeLocaleRepository facilityPriceTypeLocaleRepository) {
        this.facilityPriceTypeLocaleRepository = facilityPriceTypeLocaleRepository;
    }

    @Transactional
    @Override
    public SuccessResponse create(FacilityPriceTypeEntity facilityPriceTypeEntity,
                                  LocaleEntity localeEntity,
                                  CreateFacilityPriceTypeLocaleRequest request) {
        FacilityPriceTypeLocaleEntity entity = FacilityPriceTypeLocaleMapper.create(request, facilityPriceTypeEntity, localeEntity);
        facilityPriceTypeLocaleRepository.save(entity);
        log.info("FacilityPriceTypeLocale created with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional(readOnly = true)
    @Override
    public FacilityPriceTypeLocaleEntity getEntityById(Long facilityPriceTypeId, Long id) {
        return facilityPriceTypeLocaleRepository
                .findByFacilityPriceTypeEntity_IdAndIdAndIsActiveAndIsDeleted(facilityPriceTypeId, id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("FacilityPriceTypeLocale not found with id: " + id));
    }

    @Transactional
    @Override
    public SuccessResponse update(FacilityPriceTypeLocaleEntity entity, UpdateFacilityPriceTypeLocaleRequest request) {
        FacilityPriceTypeLocaleMapper.update(entity, request);
        facilityPriceTypeLocaleRepository.save(entity);
        log.info("FacilityPriceTypeLocale updated with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse delete(FacilityPriceTypeLocaleEntity entity) {
        entity.setIsDeleted(true);
        entity.setIsActive(false);
        facilityPriceTypeLocaleRepository.save(entity);
        log.info("FacilityPriceTypeLocale soft-deleted with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }
}
