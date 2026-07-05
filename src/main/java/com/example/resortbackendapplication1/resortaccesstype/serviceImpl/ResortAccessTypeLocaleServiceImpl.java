package com.example.resortbackendapplication1.resortaccesstype.serviceImpl;

import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.resortaccesstype.dto.request.locale.CreateResortAccessTypeLocaleRequest;
import com.example.resortbackendapplication1.resortaccesstype.dto.request.locale.UpdateResortAccessTypeLocaleRequest;
import com.example.resortbackendapplication1.resortaccesstype.model.entity.ResortAccessTypeEntity;
import com.example.resortbackendapplication1.resortaccesstype.model.entity.ResortAccessTypeLocaleEntity;
import com.example.resortbackendapplication1.resortaccesstype.model.mapper.ResortAccessTypeLocaleMapper;
import com.example.resortbackendapplication1.resortaccesstype.repository.ResortAccessTypeLocaleRepository;
import com.example.resortbackendapplication1.resortaccesstype.service.ResortAccessTypeLocaleService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class ResortAccessTypeLocaleServiceImpl implements ResortAccessTypeLocaleService {

    private final ResortAccessTypeLocaleRepository resortAccessTypeLocaleRepository;

    public ResortAccessTypeLocaleServiceImpl(ResortAccessTypeLocaleRepository resortAccessTypeLocaleRepository) {
        this.resortAccessTypeLocaleRepository = resortAccessTypeLocaleRepository;
    }

    @Transactional
    @Override
    public SuccessResponse create(ResortAccessTypeEntity resortAccessTypeEntity,
                                  LocaleEntity localeEntity,
                                  CreateResortAccessTypeLocaleRequest request) {
        ResortAccessTypeLocaleEntity entity = ResortAccessTypeLocaleMapper.create(request, resortAccessTypeEntity, localeEntity);
        resortAccessTypeLocaleRepository.save(entity);
        log.info("ResortAccessTypeLocale created with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public ResortAccessTypeLocaleEntity getEntityById(Long resortAccessTypeId, Long id) {
        return resortAccessTypeLocaleRepository
                .findByResortAccessTypeEntity_IdAndIdAndIsActiveAndIsDeleted(resortAccessTypeId, id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("ResortAccessTypeLocale not found with id: " + id));
    }

    @Transactional
    @Override
    public SuccessResponse update(ResortAccessTypeLocaleEntity entity, UpdateResortAccessTypeLocaleRequest request) {
        ResortAccessTypeLocaleMapper.update(entity, request);
        resortAccessTypeLocaleRepository.save(entity);
        log.info("ResortAccessTypeLocale updated with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse delete(ResortAccessTypeLocaleEntity entity) {
        entity.setIsDeleted(true);
        entity.setIsActive(false);
        resortAccessTypeLocaleRepository.save(entity);
        log.info("ResortAccessTypeLocale soft-deleted with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }
}
