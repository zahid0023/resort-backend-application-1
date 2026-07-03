package com.example.resortbackendapplication1.uiblocksection.serviceImpl;

import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.uiblocksection.dto.request.uiblocksection.uiblocksectionlocale.CreateUiBlockSectionLocaleRequest;
import com.example.resortbackendapplication1.uiblocksection.dto.request.uiblocksection.uiblocksectionlocale.UpdateUiBlockSectionLocaleRequest;
import com.example.resortbackendapplication1.uiblocksection.model.entity.UiBlockSectionEntity;
import com.example.resortbackendapplication1.uiblocksection.model.entity.UiBlockSectionLocaleEntity;
import com.example.resortbackendapplication1.uiblocksection.model.mapper.UiBlockSectionLocaleMapper;
import com.example.resortbackendapplication1.uiblocksection.repository.UiBlockSectionLocaleRepository;
import com.example.resortbackendapplication1.uiblocksection.service.UiBlockSectionLocaleService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class UiBlockSectionLocaleServiceImpl implements UiBlockSectionLocaleService {

    private final UiBlockSectionLocaleRepository uiBlockSectionLocaleRepository;

    public UiBlockSectionLocaleServiceImpl(UiBlockSectionLocaleRepository uiBlockSectionLocaleRepository) {
        this.uiBlockSectionLocaleRepository = uiBlockSectionLocaleRepository;
    }

    @Transactional
    @Override
    public SuccessResponse create(UiBlockSectionEntity uiBlockSectionEntity,
                                  LocaleEntity localeEntity,
                                  CreateUiBlockSectionLocaleRequest request) {
        UiBlockSectionLocaleEntity entity = UiBlockSectionLocaleMapper.create(request, uiBlockSectionEntity, localeEntity);
        uiBlockSectionLocaleRepository.save(entity);
        log.info("UiBlockSectionLocale created with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse update(UiBlockSectionLocaleEntity entity, UpdateUiBlockSectionLocaleRequest request) {
        UiBlockSectionLocaleMapper.update(entity, request);
        uiBlockSectionLocaleRepository.save(entity);
        log.info("UiBlockSectionLocale updated with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse delete(UiBlockSectionLocaleEntity entity) {
        entity.setIsDeleted(true);
        entity.setIsActive(false);
        uiBlockSectionLocaleRepository.save(entity);
        log.info("UiBlockSectionLocale soft-deleted with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public UiBlockSectionLocaleEntity getEntityById(Long uiBlockSectionId, Long id) {
        return uiBlockSectionLocaleRepository
                .findByUiBlockSectionEntity_IdAndIdAndIsActiveAndIsDeleted(uiBlockSectionId, id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("UiBlockSectionLocale not found with id: " + id));
    }
}
