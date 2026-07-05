package com.example.resortbackendapplication1.resortbasicinfo.serviceImpl;

import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.resortbasicinfo.dto.request.resortbasicinfolocale.CreateResortBasicInfoLocaleRequest;
import com.example.resortbackendapplication1.resortbasicinfo.dto.request.resortbasicinfolocale.UpdateResortBasicInfoLocaleRequest;
import com.example.resortbackendapplication1.resortbasicinfo.model.entity.ResortBasicInfoEntity;
import com.example.resortbackendapplication1.resortbasicinfo.model.entity.ResortBasicInfoLocaleEntity;
import com.example.resortbackendapplication1.resortbasicinfo.model.mapper.ResortBasicInfoLocaleMapper;
import com.example.resortbackendapplication1.resortbasicinfo.repository.ResortBasicInfoLocaleRepository;
import com.example.resortbackendapplication1.resortbasicinfo.service.ResortBasicInfoLocaleService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class ResortBasicInfoLocaleServiceImpl implements ResortBasicInfoLocaleService {

    private final ResortBasicInfoLocaleRepository repository;

    public ResortBasicInfoLocaleServiceImpl(ResortBasicInfoLocaleRepository repository) {
        this.repository = repository;
    }

    @Transactional
    @Override
    public SuccessResponse create(ResortBasicInfoEntity resortBasicInfoEntity,
                                  LocaleEntity localeEntity,
                                  CreateResortBasicInfoLocaleRequest request) {
        ResortBasicInfoLocaleEntity entity = ResortBasicInfoLocaleMapper.create(request, resortBasicInfoEntity, localeEntity);
        repository.save(entity);
        log.info("ResortBasicInfoLocale created with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public ResortBasicInfoLocaleEntity getEntityById(Long resortBasicInfoId, Long id) {
        return repository.findByResortBasicInfoEntity_IdAndIdAndIsActiveAndIsDeleted(resortBasicInfoId, id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("ResortBasicInfoLocale not found with id: " + id));
    }

    @Transactional
    @Override
    public SuccessResponse update(ResortBasicInfoLocaleEntity entity, UpdateResortBasicInfoLocaleRequest request) {
        ResortBasicInfoLocaleMapper.update(entity, request);
        repository.save(entity);
        log.info("ResortBasicInfoLocale updated with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse delete(ResortBasicInfoLocaleEntity entity) {
        entity.setIsDeleted(true);
        entity.setIsActive(false);
        repository.save(entity);
        log.info("ResortBasicInfoLocale soft-deleted with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }
}
