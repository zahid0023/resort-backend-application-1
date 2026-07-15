package com.example.resortbackendapplication1.resort.serviceImpl;

import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.resort.dto.request.resortfacilitygroup.resortfacilitygrouplocale.CreateResortFacilityGroupLocaleRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortfacilitygroup.resortfacilitygrouplocale.UpdateResortFacilityGroupLocaleRequest;
import com.example.resortbackendapplication1.resort.model.entity.ResortFacilityGroupEntity;
import com.example.resortbackendapplication1.resort.model.entity.ResortFacilityGroupLocaleEntity;
import com.example.resortbackendapplication1.resort.model.mapper.ResortFacilityGroupLocaleMapper;
import com.example.resortbackendapplication1.resort.repository.ResortFacilityGroupLocaleRepository;
import com.example.resortbackendapplication1.resort.service.ResortFacilityGroupLocaleService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class ResortFacilityGroupLocaleServiceImpl implements ResortFacilityGroupLocaleService {

    private final ResortFacilityGroupLocaleRepository resortFacilityGroupLocaleRepository;

    public ResortFacilityGroupLocaleServiceImpl(ResortFacilityGroupLocaleRepository resortFacilityGroupLocaleRepository) {
        this.resortFacilityGroupLocaleRepository = resortFacilityGroupLocaleRepository;
    }

    @Transactional
    @Override
    public SuccessResponse create(ResortFacilityGroupEntity resortFacilityGroupEntity,
                                  LocaleEntity localeEntity,
                                  CreateResortFacilityGroupLocaleRequest request) {
        ResortFacilityGroupLocaleEntity entity = ResortFacilityGroupLocaleMapper.create(request, resortFacilityGroupEntity, localeEntity);
        resortFacilityGroupLocaleRepository.save(entity);
        log.info("ResortFacilityGroupLocale created with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public ResortFacilityGroupLocaleEntity getEntityById(Long resortFacilityGroupId, Long id) {
        return resortFacilityGroupLocaleRepository
                .findByResortFacilityGroupEntity_IdAndIdAndIsActiveAndIsDeleted(resortFacilityGroupId, id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("ResortFacilityGroupLocale not found with id: " + id));
    }

    @Transactional
    @Override
    public SuccessResponse update(ResortFacilityGroupLocaleEntity entity,
                                  UpdateResortFacilityGroupLocaleRequest request) {
        ResortFacilityGroupLocaleMapper.update(entity, request);
        resortFacilityGroupLocaleRepository.save(entity);
        log.info("ResortFacilityGroupLocale updated with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse delete(ResortFacilityGroupLocaleEntity entity) {
        entity.setIsDeleted(true);
        entity.setIsActive(false);
        resortFacilityGroupLocaleRepository.save(entity);
        log.info("ResortFacilityGroupLocale soft-deleted with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }
}
