package com.example.resortbackendapplication1.resortroomcategory.serviceImpl;

import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.resortroomcategory.dto.request.locale.CreateResortRoomCategoryLocaleRequest;
import com.example.resortbackendapplication1.resortroomcategory.dto.request.locale.UpdateResortRoomCategoryLocaleRequest;
import com.example.resortbackendapplication1.resortroomcategory.model.entity.ResortRoomCategoryEntity;
import com.example.resortbackendapplication1.resortroomcategory.model.entity.ResortRoomCategoryLocaleEntity;
import com.example.resortbackendapplication1.resortroomcategory.model.mapper.ResortRoomCategoryLocaleMapper;
import com.example.resortbackendapplication1.resortroomcategory.repository.ResortRoomCategoryLocaleRepository;
import com.example.resortbackendapplication1.resortroomcategory.service.ResortRoomCategoryLocaleService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class ResortRoomCategoryLocaleServiceImpl implements ResortRoomCategoryLocaleService {

    private final ResortRoomCategoryLocaleRepository resortRoomCategoryLocaleRepository;

    public ResortRoomCategoryLocaleServiceImpl(ResortRoomCategoryLocaleRepository resortRoomCategoryLocaleRepository) {
        this.resortRoomCategoryLocaleRepository = resortRoomCategoryLocaleRepository;
    }

    @Transactional
    @Override
    public SuccessResponse create(ResortRoomCategoryEntity resortRoomCategoryEntity,
                                   LocaleEntity localeEntity,
                                   CreateResortRoomCategoryLocaleRequest request) {
        ResortRoomCategoryLocaleEntity entity = ResortRoomCategoryLocaleMapper.create(request, resortRoomCategoryEntity, localeEntity);
        resortRoomCategoryLocaleRepository.save(entity);
        log.info("ResortRoomCategoryLocale created with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public ResortRoomCategoryLocaleEntity getEntityById(Long resortRoomCategoryId, Long id) {
        return resortRoomCategoryLocaleRepository
                .findByResortRoomCategoryEntity_IdAndIdAndIsActiveAndIsDeleted(resortRoomCategoryId, id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("ResortRoomCategoryLocale not found with id: " + id));
    }

    @Transactional
    @Override
    public SuccessResponse update(ResortRoomCategoryLocaleEntity entity, UpdateResortRoomCategoryLocaleRequest request) {
        ResortRoomCategoryLocaleMapper.update(entity, request);
        resortRoomCategoryLocaleRepository.save(entity);
        log.info("ResortRoomCategoryLocale updated with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse delete(ResortRoomCategoryLocaleEntity entity) {
        entity.setIsDeleted(true);
        entity.setIsActive(false);
        resortRoomCategoryLocaleRepository.save(entity);
        log.info("ResortRoomCategoryLocale soft-deleted with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }
}
