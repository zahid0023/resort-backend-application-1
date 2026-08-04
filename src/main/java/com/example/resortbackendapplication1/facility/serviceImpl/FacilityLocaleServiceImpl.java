package com.example.resortbackendapplication1.facility.serviceImpl;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.commons.utils.Pagination;
import com.example.resortbackendapplication1.facility.dto.request.facility.locale.CreateFacilityLocaleRequest;
import com.example.resortbackendapplication1.facility.dto.request.facility.locale.UpdateFacilityLocaleRequest;
import com.example.resortbackendapplication1.facility.model.dto.FacilityLocaleDto;
import com.example.resortbackendapplication1.facility.model.entity.FacilityEntity;
import com.example.resortbackendapplication1.facility.model.entity.FacilityLocaleEntity;
import com.example.resortbackendapplication1.facility.model.mapper.FacilityLocaleMapper;
import com.example.resortbackendapplication1.facility.repository.FacilityLocaleRepository;
import com.example.resortbackendapplication1.facility.service.FacilityLocaleService;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
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
public class FacilityLocaleServiceImpl implements FacilityLocaleService {
    private final FacilityLocaleRepository facilityLocaleRepository;

    public FacilityLocaleServiceImpl(FacilityLocaleRepository facilityLocaleRepository) {
        this.facilityLocaleRepository = facilityLocaleRepository;
    }

    @Transactional
    @Override
    public SuccessResponse create(CreateFacilityLocaleRequest request,
                                  FacilityEntity facilityEntity,
                                  LocaleEntity localeEntity) {
        if (facilityLocaleRepository.existsByFacilityEntity_IdAndLocaleEntity_IdAndIsActiveAndIsDeleted(
                facilityEntity.getId(), localeEntity.getId(), true, false)) {
            throw new IllegalStateException("Facility already has a locale entry for locale id: " + localeEntity.getId());
        }

        if (facilityLocaleRepository.existsByLocaleEntity_IdAndNameAndIsActiveAndIsDeleted(
                localeEntity.getId(), request.getName(), true, false)) {
            throw new IllegalStateException("FacilityLocale with name '" + request.getName()
                    + "' already exists for localeId '" + localeEntity.getId() + "'");
        }

        FacilityLocaleEntity entity = FacilityLocaleMapper.create(request);
        facilityEntity.addFacilityLocaleEntity(entity);
        localeEntity.addFacilityLocaleEntity(entity);
        facilityLocaleRepository.save(entity);
        log.info("FacilityLocale created with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse update(FacilityLocaleEntity entity,
                                  UpdateFacilityLocaleRequest request) {
        if (facilityLocaleRepository.existsByLocaleEntity_IdAndNameAndIdNotAndIsActiveAndIsDeleted(
                entity.getLocaleEntity().getId(), request.getName(), entity.getId(), true, false)) {
            throw new IllegalStateException("FacilityLocale with name '" + request.getName()
                    + "' already exists for localeId '" + entity.getLocaleEntity().getId() + "'");
        }

        FacilityLocaleMapper.update(entity, request);
        facilityLocaleRepository.save(entity);
        log.info("FacilityLocale updated with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse delete(FacilityLocaleEntity entity) {
        entity.setIsDeleted(true);
        entity.setIsActive(false);
        facilityLocaleRepository.save(entity);
        log.info("FacilityLocale soft-deleted with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public FacilityLocaleEntity getEntityById(Long facilityId, Long id) {
        return facilityLocaleRepository
                .findByFacilityEntity_IdAndIdAndIsActiveAndIsDeleted(facilityId, id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("FacilityLocale not found with id: " + id));
    }

    @Override
    public PaginatedResponse<FacilityLocaleDto> getAll(Long facilityId, String localeCode, PaginatedRequest paginatedRequest) {
        Pageable pageable = paginatedRequest.toPageable(Set.of());
        Page<@NonNull FacilityLocaleDto> dtoPage = (localeCode == null || localeCode.isBlank()
                ? facilityLocaleRepository.findByFacilityEntity_IdAndIsActiveAndIsDeleted(facilityId, true, false, pageable)
                : facilityLocaleRepository.findByFacilityEntity_IdAndLocaleEntity_CodeContainingIgnoreCaseAndIsActiveAndIsDeleted(
                        facilityId, localeCode, true, false, pageable))
                .map(FacilityLocaleMapper::toDto);
        return Pagination.buildPaginatedResponse(dtoPage);
    }
}
