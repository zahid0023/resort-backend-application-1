package com.example.resortbackendapplication1.price.serviceImpl;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.commons.utils.Pagination;
import com.example.resortbackendapplication1.price.dto.request.resortfacilitypricetype.locale.CreateFacilityPriceTypeLocaleRequest;
import com.example.resortbackendapplication1.price.dto.request.resortfacilitypricetype.locale.UpdateFacilityPriceTypeLocaleRequest;
import com.example.resortbackendapplication1.price.model.dto.FacilityPriceTypeLocaleDto;
import com.example.resortbackendapplication1.price.model.entity.FacilityPriceTypeEntity;
import com.example.resortbackendapplication1.price.model.entity.FacilityPriceTypeLocaleEntity;
import com.example.resortbackendapplication1.price.model.mapper.FacilityPriceTypeLocaleMapper;
import com.example.resortbackendapplication1.price.repository.FacilityPriceTypeLocaleRepository;
import com.example.resortbackendapplication1.price.service.FacilityPriceTypeLocaleService;
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
public class FacilityPriceTypeLocaleServiceImpl implements FacilityPriceTypeLocaleService {
    private final FacilityPriceTypeLocaleRepository facilityPriceTypeLocaleRepository;

    public FacilityPriceTypeLocaleServiceImpl(FacilityPriceTypeLocaleRepository facilityPriceTypeLocaleRepository) {
        this.facilityPriceTypeLocaleRepository = facilityPriceTypeLocaleRepository;
    }

    @Transactional
    @Override
    public SuccessResponse create(CreateFacilityPriceTypeLocaleRequest request,
                                  FacilityPriceTypeEntity facilityPriceTypeEntity,
                                  LocaleEntity localeEntity) {
        if (facilityPriceTypeLocaleRepository.existsByFacilityPriceTypeEntity_IdAndLocaleEntity_IdAndIsActiveAndIsDeleted(
                facilityPriceTypeEntity.getId(), localeEntity.getId(), true, false)) {
            throw new IllegalStateException("FacilityPriceTypeLocale already exists for facilityPriceTypeId '"
                    + facilityPriceTypeEntity.getId() + "' and localeId '" + localeEntity.getId() + "'");
        }

        if (facilityPriceTypeLocaleRepository.existsByLocaleEntity_IdAndNameAndIsActiveAndIsDeleted(
                localeEntity.getId(), request.getName(), true, false)) {
            throw new IllegalStateException("FacilityPriceTypeLocale with name '" + request.getName()
                    + "' already exists for localeId '" + localeEntity.getId() + "'");
        }

        FacilityPriceTypeLocaleEntity entity = FacilityPriceTypeLocaleMapper.create(request);
        facilityPriceTypeEntity.addFacilityPriceTypeLocaleEntity(entity);
        localeEntity.addFacilityPriceTypeLocaleEntity(entity);
        facilityPriceTypeLocaleRepository.save(entity);
        log.info("FacilityPriceTypeLocale created with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse update(FacilityPriceTypeLocaleEntity entity,
                                  UpdateFacilityPriceTypeLocaleRequest request) {
        if (facilityPriceTypeLocaleRepository.existsByLocaleEntity_IdAndNameAndIdNotAndIsActiveAndIsDeleted(
                entity.getLocaleEntity().getId(), request.getName(), entity.getId(), true, false)) {
            throw new IllegalStateException("FacilityPriceTypeLocale with name '" + request.getName()
                    + "' already exists for localeId '" + entity.getLocaleEntity().getId() + "'");
        }

        FacilityPriceTypeLocaleMapper.update(entity, request);
        facilityPriceTypeLocaleRepository.save(entity);
        log.info("FacilityPriceTypeLocale updated with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse delete(FacilityPriceTypeLocaleEntity entity) {
        entity.setIsDeleted(true);
        entity.setIsActive(false);
        facilityPriceTypeLocaleRepository.save(entity);
        log.info("FacilityPriceTypeLocale soft-deleted with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public FacilityPriceTypeLocaleEntity getEntityById(Long facilityPriceTypeId, Long id) {
        return facilityPriceTypeLocaleRepository
                .findByFacilityPriceTypeEntity_IdAndIdAndIsActiveAndIsDeleted(facilityPriceTypeId, id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("FacilityPriceTypeLocale not found with id: " + id));
    }

    @Override
    public PaginatedResponse<FacilityPriceTypeLocaleDto> getAll(Long facilityPriceTypeId, String localeCode, PaginatedRequest paginatedRequest) {
        Pageable pageable = paginatedRequest.toPageable(Set.of());
        Page<@NonNull FacilityPriceTypeLocaleDto> dtoPage = (localeCode == null || localeCode.isBlank()
                ? facilityPriceTypeLocaleRepository.findByFacilityPriceTypeEntity_IdAndIsActiveAndIsDeleted(facilityPriceTypeId, true, false, pageable)
                : facilityPriceTypeLocaleRepository.findByFacilityPriceTypeEntity_IdAndLocaleEntity_CodeContainingIgnoreCaseAndIsActiveAndIsDeleted(
                        facilityPriceTypeId, localeCode, true, false, pageable))
                .map(FacilityPriceTypeLocaleMapper::toDto);
        return Pagination.buildPaginatedResponse(dtoPage);
    }

    @Override
    public LocaleCountResponse getCount(Long facilityPriceTypeId) {
        List<String> codes = facilityPriceTypeLocaleRepository
                .findLocaleEntity_CodeByFacilityPriceTypeEntity_IdAndIsActiveAndIsDeleted(facilityPriceTypeId, true, false);
        return new LocaleCountResponse((long) codes.size(), codes);
    }
}
