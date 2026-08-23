package com.example.resortbackendapplication1.resort.serviceImpl;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.commons.utils.Pagination;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.resort.dto.request.resortroomcategory.locale.CreateResortRoomCategoryLocaleRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortroomcategory.locale.UpdateResortRoomCategoryLocaleRequest;
import com.example.resortbackendapplication1.resort.model.dto.ResortRoomCategoryLocaleDto;
import com.example.resortbackendapplication1.resort.model.entity.ResortRoomCategoryEntity;
import com.example.resortbackendapplication1.resort.model.entity.ResortRoomCategoryLocaleEntity;
import com.example.resortbackendapplication1.resort.model.mapper.ResortRoomCategoryLocaleMapper;
import com.example.resortbackendapplication1.resort.repository.ResortRoomCategoryLocaleRepository;
import com.example.resortbackendapplication1.resort.service.ResortRoomCategoryLocaleService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@Slf4j
public class ResortRoomCategoryLocaleServiceImpl implements ResortRoomCategoryLocaleService {

    private final ResortRoomCategoryLocaleRepository resortRoomCategoryLocaleRepository;

    public ResortRoomCategoryLocaleServiceImpl(ResortRoomCategoryLocaleRepository resortRoomCategoryLocaleRepository) {
        this.resortRoomCategoryLocaleRepository = resortRoomCategoryLocaleRepository;
    }

    @Transactional
    @Override
    public SuccessResponse create(CreateResortRoomCategoryLocaleRequest request,
                                  ResortRoomCategoryEntity resortRoomCategoryEntity,
                                  LocaleEntity localeEntity) {
        if (resortRoomCategoryLocaleRepository.existsByResortRoomCategoryEntity_IdAndLocaleEntity_IdAndIsActiveAndIsDeleted(
                resortRoomCategoryEntity.getId(), localeEntity.getId(), true, false)) {
            throw new IllegalStateException("ResortRoomCategory already has a locale entry for locale id: " + localeEntity.getId());
        }

        ResortRoomCategoryLocaleEntity entity = ResortRoomCategoryLocaleMapper.create(request);
        resortRoomCategoryEntity.addResortRoomCategoryLocaleEntity(entity);
        localeEntity.addResortRoomCategoryLocaleEntity(entity);
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

    @Override
    public PaginatedResponse<ResortRoomCategoryLocaleDto> getAll(Long resortRoomCategoryId, String localeCode, PaginatedRequest paginatedRequest) {
        Pageable pageable = paginatedRequest.toPageable(Set.of());
        Page<@NonNull ResortRoomCategoryLocaleDto> dtoPage = (localeCode == null || localeCode.isBlank()
                ? resortRoomCategoryLocaleRepository.findByResortRoomCategoryEntity_IdAndIsActiveAndIsDeleted(resortRoomCategoryId, true, false, pageable)
                : resortRoomCategoryLocaleRepository.findByResortRoomCategoryEntity_IdAndLocaleEntity_CodeContainingIgnoreCaseAndIsActiveAndIsDeleted(
                        resortRoomCategoryId, localeCode, true, false, pageable))
                .map(ResortRoomCategoryLocaleMapper::toDto);
        return Pagination.buildPaginatedResponse(dtoPage);
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
