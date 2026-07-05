package com.example.resortbackendapplication1.resortpermissiontype.serviceImpl;

import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.resortpermissiontype.dto.request.locale.CreateResortPermissionTypeLocaleRequest;
import com.example.resortbackendapplication1.resortpermissiontype.dto.request.locale.UpdateResortPermissionTypeLocaleRequest;
import com.example.resortbackendapplication1.resortpermissiontype.model.entity.ResortPermissionTypeEntity;
import com.example.resortbackendapplication1.resortpermissiontype.model.entity.ResortPermissionTypeLocaleEntity;
import com.example.resortbackendapplication1.resortpermissiontype.model.mapper.ResortPermissionTypeLocaleMapper;
import com.example.resortbackendapplication1.resortpermissiontype.repository.ResortPermissionTypeLocaleRepository;
import com.example.resortbackendapplication1.resortpermissiontype.service.ResortPermissionTypeLocaleService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class ResortPermissionTypeLocaleServiceImpl implements ResortPermissionTypeLocaleService {

    private final ResortPermissionTypeLocaleRepository resortPermissionTypeLocaleRepository;

    public ResortPermissionTypeLocaleServiceImpl(ResortPermissionTypeLocaleRepository resortPermissionTypeLocaleRepository) {
        this.resortPermissionTypeLocaleRepository = resortPermissionTypeLocaleRepository;
    }

    @Transactional
    @Override
    public SuccessResponse create(ResortPermissionTypeEntity resortPermissionTypeEntity,
                                  LocaleEntity localeEntity,
                                  CreateResortPermissionTypeLocaleRequest request) {
        ResortPermissionTypeLocaleEntity entity = ResortPermissionTypeLocaleMapper.create(request, resortPermissionTypeEntity, localeEntity);
        resortPermissionTypeLocaleRepository.save(entity);
        log.info("ResortPermissionTypeLocale created with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public ResortPermissionTypeLocaleEntity getEntityById(Long resortPermissionTypeId, Long id) {
        return resortPermissionTypeLocaleRepository
                .findByResortPermissionTypeEntity_IdAndIdAndIsActiveAndIsDeleted(resortPermissionTypeId, id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("ResortPermissionTypeLocale not found with id: " + id));
    }

    @Transactional
    @Override
    public SuccessResponse update(ResortPermissionTypeLocaleEntity entity, UpdateResortPermissionTypeLocaleRequest request) {
        ResortPermissionTypeLocaleMapper.update(entity, request);
        resortPermissionTypeLocaleRepository.save(entity);
        log.info("ResortPermissionTypeLocale updated with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse delete(ResortPermissionTypeLocaleEntity entity) {
        entity.setIsDeleted(true);
        entity.setIsActive(false);
        resortPermissionTypeLocaleRepository.save(entity);
        log.info("ResortPermissionTypeLocale soft-deleted with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }
}
