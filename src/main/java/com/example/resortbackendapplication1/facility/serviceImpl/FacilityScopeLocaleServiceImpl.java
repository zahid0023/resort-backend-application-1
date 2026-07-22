package com.example.resortbackendapplication1.facility.serviceImpl;

import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.facility.dto.request.facilityscopes.facilityscopelocale.CreateFacilityScopeLocaleRequest;
import com.example.resortbackendapplication1.facility.dto.request.facilityscopes.facilityscopelocale.UpdateFacilityScopeLocaleRequest;
import com.example.resortbackendapplication1.facility.model.entity.FacilityScopeEntity;
import com.example.resortbackendapplication1.facility.model.entity.FacilityScopeLocaleEntity;
import com.example.resortbackendapplication1.facility.model.mapper.FacilityScopeLocaleMapper;
import com.example.resortbackendapplication1.facility.repository.FacilityScopeLocaleRepository;
import com.example.resortbackendapplication1.facility.service.FacilityScopeLocaleService;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class FacilityScopeLocaleServiceImpl implements FacilityScopeLocaleService {

    private final FacilityScopeLocaleRepository facilityScopeLocaleRepository;

    public FacilityScopeLocaleServiceImpl(FacilityScopeLocaleRepository facilityScopeLocaleRepository) {
        this.facilityScopeLocaleRepository = facilityScopeLocaleRepository;
    }

    @Transactional
    @Override
    public SuccessResponse create(FacilityScopeEntity facilityScopeEntity,
                                  LocaleEntity localeEntity,
                                  CreateFacilityScopeLocaleRequest request) {
        FacilityScopeLocaleEntity entity = FacilityScopeLocaleMapper.create(request, facilityScopeEntity, localeEntity);
        facilityScopeLocaleRepository.save(entity);
        log.info("FacilityScopeLocale created with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional(readOnly = true)
    @Override
    public FacilityScopeLocaleEntity getEntityById(Long facilityScopeId, Long id) {
        return facilityScopeLocaleRepository
                .findByFacilityScopeEntity_IdAndIdAndIsActiveAndIsDeleted(facilityScopeId, id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("FacilityScopeLocale not found with id: " + id));
    }

    @Transactional
    @Override
    public SuccessResponse update(FacilityScopeLocaleEntity entity, UpdateFacilityScopeLocaleRequest request) {
        FacilityScopeLocaleMapper.update(entity, request);
        facilityScopeLocaleRepository.save(entity);
        log.info("FacilityScopeLocale updated with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse delete(FacilityScopeLocaleEntity entity) {
        entity.setIsDeleted(true);
        entity.setIsActive(false);
        facilityScopeLocaleRepository.save(entity);
        log.info("FacilityScopeLocale soft-deleted with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }
}
