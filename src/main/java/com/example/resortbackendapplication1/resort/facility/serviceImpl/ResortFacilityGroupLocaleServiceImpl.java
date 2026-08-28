package com.example.resortbackendapplication1.resort.facility.serviceImpl;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.commons.utils.Pagination;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.resort.facility.dto.request.resortfacilitygroup.locale.CreateResortFacilityGroupLocaleRequest;
import com.example.resortbackendapplication1.resort.facility.dto.request.resortfacilitygroup.locale.UpdateResortFacilityGroupLocaleRequest;
import com.example.resortbackendapplication1.resort.facility.dto.response.resortfacilitygrouplocales.ResortFacilityGroupLocaleCountResponse;
import com.example.resortbackendapplication1.resort.facility.model.dto.ResortFacilityGroupLocaleDto;
import com.example.resortbackendapplication1.resort.facility.model.entity.ResortFacilityGroupEntity;
import com.example.resortbackendapplication1.resort.facility.model.entity.ResortFacilityGroupLocaleEntity;
import com.example.resortbackendapplication1.resort.facility.model.mapper.ResortFacilityGroupLocaleMapper;
import com.example.resortbackendapplication1.resort.facility.repository.ResortFacilityGroupLocaleRepository;
import com.example.resortbackendapplication1.resort.facility.service.ResortFacilityGroupLocaleService;
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
public class ResortFacilityGroupLocaleServiceImpl implements ResortFacilityGroupLocaleService {

    private final ResortFacilityGroupLocaleRepository resortFacilityGroupLocaleRepository;

    public ResortFacilityGroupLocaleServiceImpl(ResortFacilityGroupLocaleRepository resortFacilityGroupLocaleRepository) {
        this.resortFacilityGroupLocaleRepository = resortFacilityGroupLocaleRepository;
    }

    @Transactional
    @Override
    public SuccessResponse create(CreateResortFacilityGroupLocaleRequest request,
                                  ResortFacilityGroupEntity resortFacilityGroupEntity,
                                  LocaleEntity localeEntity) {
        if (resortFacilityGroupLocaleRepository.existsByResortFacilityGroupEntity_IdAndLocaleEntity_IdAndIsActiveAndIsDeleted(
                resortFacilityGroupEntity.getId(), localeEntity.getId(), true, false)) {
            throw new IllegalStateException("ResortFacilityGroup already has a locale entry for locale id: " + localeEntity.getId());
        }

        ResortFacilityGroupLocaleEntity entity = ResortFacilityGroupLocaleMapper.create(request);
        resortFacilityGroupEntity.addResortFacilityGroupLocaleEntity(entity);
        localeEntity.addResortFacilityGroupLocaleEntity(entity);
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

    @Override
    public PaginatedResponse<ResortFacilityGroupLocaleDto> getAll(Long resortFacilityGroupId, String localeCode, PaginatedRequest paginatedRequest) {
        Pageable pageable = paginatedRequest.toPageable(Set.of());
        Page<@NonNull ResortFacilityGroupLocaleDto> dtoPage = (localeCode == null || localeCode.isBlank()
                ? resortFacilityGroupLocaleRepository.findByResortFacilityGroupEntity_IdAndIsActiveAndIsDeleted(resortFacilityGroupId, true, false, pageable)
                : resortFacilityGroupLocaleRepository.findByResortFacilityGroupEntity_IdAndLocaleEntity_CodeContainingIgnoreCaseAndIsActiveAndIsDeleted(
                        resortFacilityGroupId, localeCode, true, false, pageable))
                .map(ResortFacilityGroupLocaleMapper::toDto);
        return Pagination.buildPaginatedResponse(dtoPage);
    }

    @Override
    public ResortFacilityGroupLocaleCountResponse getActiveCount(Long resortFacilityGroupId) {
        List<String> codes = resortFacilityGroupLocaleRepository
                .findLocaleCodeByResortFacilityGroupEntity_IdAndIsActiveAndIsDeleted(resortFacilityGroupId, true, false);
        return new ResortFacilityGroupLocaleCountResponse((long) codes.size(), codes);
    }

    @Transactional
    @Override
    public SuccessResponse update(ResortFacilityGroupLocaleEntity entity, UpdateResortFacilityGroupLocaleRequest request) {
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
