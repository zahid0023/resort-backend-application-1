package com.example.resortbackendapplication1.resort.serviceImpl;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.commons.utils.Pagination;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.resort.dto.request.resortfacility.locale.CreateResortFacilityLocaleRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortfacility.locale.UpdateResortFacilityLocaleRequest;
import com.example.resortbackendapplication1.resort.model.dto.ResortFacilityLocaleDto;
import com.example.resortbackendapplication1.resort.model.entity.ResortFacilityEntity;
import com.example.resortbackendapplication1.resort.model.entity.ResortFacilityLocaleEntity;
import com.example.resortbackendapplication1.resort.model.mapper.ResortFacilityLocaleMapper;
import com.example.resortbackendapplication1.resort.repository.ResortFacilityLocaleRepository;
import com.example.resortbackendapplication1.resort.service.ResortFacilityLocaleService;
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
public class ResortFacilityLocaleServiceImpl implements ResortFacilityLocaleService {

    private final ResortFacilityLocaleRepository resortFacilityLocaleRepository;

    public ResortFacilityLocaleServiceImpl(ResortFacilityLocaleRepository resortFacilityLocaleRepository) {
        this.resortFacilityLocaleRepository = resortFacilityLocaleRepository;
    }

    @Transactional
    @Override
    public SuccessResponse create(CreateResortFacilityLocaleRequest request,
                                  ResortFacilityEntity resortFacilityEntity,
                                  LocaleEntity localeEntity) {
        if (resortFacilityLocaleRepository.existsByResortFacilityEntity_IdAndLocaleEntity_IdAndIsActiveAndIsDeleted(
                resortFacilityEntity.getId(), localeEntity.getId(), true, false)) {
            throw new IllegalStateException("ResortFacility already has a locale entry for locale id: " + localeEntity.getId());
        }

        ResortFacilityLocaleEntity entity = ResortFacilityLocaleMapper.create(request);
        resortFacilityEntity.addResortFacilityLocaleEntity(entity);
        localeEntity.addResortFacilityLocaleEntity(entity);
        resortFacilityLocaleRepository.save(entity);
        log.info("ResortFacilityLocale created with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public ResortFacilityLocaleEntity getEntityById(Long resortFacilityId, Long id) {
        return resortFacilityLocaleRepository
                .findByResortFacilityEntity_IdAndIdAndIsActiveAndIsDeleted(resortFacilityId, id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("ResortFacilityLocale not found with id: " + id));
    }

    @Override
    public PaginatedResponse<ResortFacilityLocaleDto> getAll(Long resortFacilityId, String localeCode, PaginatedRequest paginatedRequest) {
        Pageable pageable = paginatedRequest.toPageable(Set.of());
        Page<@NonNull ResortFacilityLocaleDto> dtoPage = (localeCode == null || localeCode.isBlank()
                ? resortFacilityLocaleRepository.findByResortFacilityEntity_IdAndIsActiveAndIsDeleted(resortFacilityId, true, false, pageable)
                : resortFacilityLocaleRepository.findByResortFacilityEntity_IdAndLocaleEntity_CodeContainingIgnoreCaseAndIsActiveAndIsDeleted(
                        resortFacilityId, localeCode, true, false, pageable))
                .map(ResortFacilityLocaleMapper::toDto);
        return Pagination.buildPaginatedResponse(dtoPage);
    }

    @Transactional
    @Override
    public SuccessResponse update(ResortFacilityLocaleEntity entity, UpdateResortFacilityLocaleRequest request) {
        ResortFacilityLocaleMapper.update(entity, request);
        resortFacilityLocaleRepository.save(entity);
        log.info("ResortFacilityLocale updated with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse delete(ResortFacilityLocaleEntity entity) {
        entity.setIsDeleted(true);
        entity.setIsActive(false);
        resortFacilityLocaleRepository.save(entity);
        log.info("ResortFacilityLocale soft-deleted with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }
}
