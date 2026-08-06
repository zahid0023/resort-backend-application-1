package com.example.resortbackendapplication1.facility.serviceImpl;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.commons.utils.Pagination;
import com.example.resortbackendapplication1.facility.dto.request.facilitygroup.locale.CreateFacilityGroupLocaleRequest;
import com.example.resortbackendapplication1.facility.dto.request.facilitygroup.locale.UpdateFacilityGroupLocaleRequest;
import com.example.resortbackendapplication1.facility.model.dto.FacilityGroupLocaleDto;
import com.example.resortbackendapplication1.facility.model.entity.FacilityGroupEntity;
import com.example.resortbackendapplication1.facility.model.entity.FacilityGroupLocaleEntity;
import com.example.resortbackendapplication1.facility.model.mapper.FacilityGroupLocaleMapper;
import com.example.resortbackendapplication1.facility.repository.FacilityGroupLocaleRepository;
import com.example.resortbackendapplication1.facility.service.FacilityGroupLocaleService;
import com.example.resortbackendapplication1.locale.dto.response.locales.LocaleCountResponse;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
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
public class FacilityGroupLocaleServiceImpl implements FacilityGroupLocaleService {
    private final FacilityGroupLocaleRepository facilityGroupLocaleRepository;

    public FacilityGroupLocaleServiceImpl(FacilityGroupLocaleRepository facilityGroupLocaleRepository) {
        this.facilityGroupLocaleRepository = facilityGroupLocaleRepository;
    }

    @Transactional
    @Override
    public SuccessResponse create(CreateFacilityGroupLocaleRequest request,
                                  FacilityGroupEntity facilityGroupEntity,
                                  LocaleEntity localeEntity) {
        if (facilityGroupLocaleRepository.existsByFacilityGroupEntity_IdAndLocaleEntity_IdAndIsActiveAndIsDeleted(
                facilityGroupEntity.getId(), localeEntity.getId(), true, false)) {
            throw new IllegalStateException("FacilityGroupLocale already exists for facilityGroupId '"
                    + facilityGroupEntity.getId() + "' and localeId '" + localeEntity.getId() + "'");
        }

        if (facilityGroupLocaleRepository.existsByLocaleEntity_IdAndNameAndIsActiveAndIsDeleted(
                localeEntity.getId(), request.getName(), true, false)) {
            throw new IllegalStateException("FacilityGroupLocale with name '" + request.getName()
                    + "' already exists for localeId '" + localeEntity.getId() + "'");
        }

        FacilityGroupLocaleEntity entity = FacilityGroupLocaleMapper.create(request);
        facilityGroupEntity.addFacilityGroupLocaleEntity(entity);
        localeEntity.addFacilityGroupLocaleEntity(entity);
        facilityGroupLocaleRepository.save(entity);
        log.info("FacilityGroupLocale created with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse update(FacilityGroupLocaleEntity entity,
                                  UpdateFacilityGroupLocaleRequest request) {
        if (facilityGroupLocaleRepository.existsByLocaleEntity_IdAndNameAndIdNotAndIsActiveAndIsDeleted(
                entity.getLocaleEntity().getId(), request.getName(), entity.getId(), true, false)) {
            throw new IllegalStateException("FacilityGroupLocale with name '" + request.getName()
                    + "' already exists for localeId '" + entity.getLocaleEntity().getId() + "'");
        }

        FacilityGroupLocaleMapper.update(entity, request);
        facilityGroupLocaleRepository.save(entity);
        log.info("FacilityGroupLocale updated with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse delete(FacilityGroupLocaleEntity entity) {
        entity.setIsDeleted(true);
        entity.setIsActive(false);
        facilityGroupLocaleRepository.save(entity);
        log.info("FacilityGroupLocale soft-deleted with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public FacilityGroupLocaleEntity getEntityById(Long facilityGroupId, Long id) {
        return facilityGroupLocaleRepository
                .findByFacilityGroupEntity_IdAndIdAndIsActiveAndIsDeleted(facilityGroupId, id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("FacilityGroupLocale not found with id: " + id));
    }

    @Override
    public PaginatedResponse<FacilityGroupLocaleDto> getAll(Long facilityGroupId, String localeCode, PaginatedRequest paginatedRequest) {
        Pageable pageable = paginatedRequest.toPageable(Set.of());
        Page<@NonNull FacilityGroupLocaleDto> dtoPage = (localeCode == null || localeCode.isBlank()
                ? facilityGroupLocaleRepository.findByFacilityGroupEntity_IdAndIsActiveAndIsDeleted(facilityGroupId, true, false, pageable)
                : facilityGroupLocaleRepository.findByFacilityGroupEntity_IdAndLocaleEntity_CodeContainingIgnoreCaseAndIsActiveAndIsDeleted(
                        facilityGroupId, localeCode, true, false, pageable))
                .map(FacilityGroupLocaleMapper::toDto);
        return Pagination.buildPaginatedResponse(dtoPage);
    }

    @Override
    public LocaleCountResponse getCount(Long facilityGroupId) {
        List<String> codes = facilityGroupLocaleRepository
                .findLocaleEntity_CodeByFacilityGroupEntity_IdAndIsActiveAndIsDeleted(facilityGroupId, true, false);
        return new LocaleCountResponse((long) codes.size(), codes);
    }
}
