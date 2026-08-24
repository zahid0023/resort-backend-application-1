package com.example.resortbackendapplication1.resort.serviceImpl;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.commons.utils.Pagination;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.resort.dto.request.resortbasicinfo.locale.CreateResortBasicInfoLocaleRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortbasicinfo.locale.UpdateResortBasicInfoLocaleRequest;
import com.example.resortbackendapplication1.resort.dto.response.resortbasicinfolocales.ResortBasicInfoLocaleCountResponse;
import com.example.resortbackendapplication1.resort.model.dto.ResortBasicInfoLocaleDto;
import com.example.resortbackendapplication1.resort.model.entity.ResortBasicInfoEntity;
import com.example.resortbackendapplication1.resort.model.entity.ResortBasicInfoLocaleEntity;
import com.example.resortbackendapplication1.resort.model.mapper.ResortBasicInfoLocaleMapper;
import com.example.resortbackendapplication1.resort.repository.ResortBasicInfoLocaleRepository;
import com.example.resortbackendapplication1.resort.service.ResortBasicInfoLocaleService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class ResortBasicInfoLocaleServiceImpl implements ResortBasicInfoLocaleService {

    private final ResortBasicInfoLocaleRepository resortBasicInfoLocaleRepository;

    public ResortBasicInfoLocaleServiceImpl(ResortBasicInfoLocaleRepository resortBasicInfoLocaleRepository) {
        this.resortBasicInfoLocaleRepository = resortBasicInfoLocaleRepository;
    }

    @Transactional
    @Override
    public SuccessResponse create(CreateResortBasicInfoLocaleRequest request,
                                  ResortBasicInfoEntity resortBasicInfoEntity,
                                  LocaleEntity localeEntity) {
        if (resortBasicInfoLocaleRepository.existsByResortBasicInfoEntity_IdAndLocaleEntity_IdAndIsActiveAndIsDeleted(
                resortBasicInfoEntity.getId(), localeEntity.getId(), true, false)) {
            throw new IllegalStateException("ResortBasicInfo already has a locale entry for locale id: " + localeEntity.getId());
        }

        ResortBasicInfoLocaleEntity entity = ResortBasicInfoLocaleMapper.create(request);
        resortBasicInfoEntity.addResortBasicInfoLocaleEntity(entity);
        localeEntity.addResortBasicInfoLocaleEntity(entity);
        resortBasicInfoLocaleRepository.save(entity);
        log.info("ResortBasicInfoLocale created with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public ResortBasicInfoLocaleEntity getEntityById(Long resortBasicInfoId, Long id) {
        return resortBasicInfoLocaleRepository
                .findByResortBasicInfoEntity_IdAndIdAndIsActiveAndIsDeleted(resortBasicInfoId, id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("ResortBasicInfoLocale not found with id: " + id));
    }

    @Override
    public PaginatedResponse<ResortBasicInfoLocaleDto> getAll(Long resortBasicInfoId, String localeCode, PaginatedRequest paginatedRequest) {
        Pageable pageable = paginatedRequest.toPageable(Set.of());
        Page<@NonNull ResortBasicInfoLocaleDto> dtoPage = (localeCode == null || localeCode.isBlank()
                ? resortBasicInfoLocaleRepository.findByResortBasicInfoEntity_IdAndIsActiveAndIsDeleted(resortBasicInfoId, true, false, pageable)
                : resortBasicInfoLocaleRepository.findByResortBasicInfoEntity_IdAndLocaleEntity_CodeContainingIgnoreCaseAndIsActiveAndIsDeleted(
                        resortBasicInfoId, localeCode, true, false, pageable))
                .map(ResortBasicInfoLocaleMapper::toDto);
        return Pagination.buildPaginatedResponse(dtoPage);
    }

    @Override
    public ResortBasicInfoLocaleCountResponse getActiveCount(Long resortBasicInfoId) {
        List<String> codes = resortBasicInfoLocaleRepository
                .findLocaleCodeByResortBasicInfoEntity_IdAndIsActiveAndIsDeleted(resortBasicInfoId, true, false);
        return new ResortBasicInfoLocaleCountResponse((long) codes.size(), codes);
    }

    @Transactional
    @Override
    public SuccessResponse update(ResortBasicInfoLocaleEntity entity, UpdateResortBasicInfoLocaleRequest request) {
        ResortBasicInfoLocaleMapper.update(entity, request);
        resortBasicInfoLocaleRepository.save(entity);
        log.info("ResortBasicInfoLocale updated with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse delete(ResortBasicInfoLocaleEntity entity) {
        entity.setIsDeleted(true);
        entity.setIsActive(false);
        resortBasicInfoLocaleRepository.save(entity);
        log.info("ResortBasicInfoLocale soft-deleted with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }
}
