package com.example.resortbackendapplication1.bedtype.serviceImpl;

import com.example.resortbackendapplication1.bedtype.dto.request.bedtype.locale.CreateBedTypeLocaleRequest;
import com.example.resortbackendapplication1.bedtype.dto.request.bedtype.locale.UpdateBedTypeLocaleRequest;
import com.example.resortbackendapplication1.bedtype.model.dto.BedTypeLocaleDto;
import com.example.resortbackendapplication1.bedtype.model.entity.BedTypeEntity;
import com.example.resortbackendapplication1.bedtype.model.entity.BedTypeLocaleEntity;
import com.example.resortbackendapplication1.bedtype.model.mapper.BedTypeLocaleMapper;
import com.example.resortbackendapplication1.bedtype.repository.BedTypeLocaleRepository;
import com.example.resortbackendapplication1.bedtype.service.BedTypeLocaleService;
import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.commons.utils.Pagination;
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
public class BedTypeLocaleServiceImpl implements BedTypeLocaleService {
    private final BedTypeLocaleRepository bedTypeLocaleRepository;

    public BedTypeLocaleServiceImpl(BedTypeLocaleRepository bedTypeLocaleRepository) {
        this.bedTypeLocaleRepository = bedTypeLocaleRepository;
    }

    @Transactional
    @Override
    public SuccessResponse create(CreateBedTypeLocaleRequest request,
                                  BedTypeEntity bedTypeEntity,
                                  LocaleEntity localeEntity) {
        if (bedTypeLocaleRepository.existsByBedTypeEntity_IdAndLocaleEntity_IdAndIsActiveAndIsDeleted(
                bedTypeEntity.getId(), localeEntity.getId(), true, false)) {
            throw new IllegalStateException("BedType already has a locale entry for locale id: " + localeEntity.getId());
        }

        BedTypeLocaleEntity entity = BedTypeLocaleMapper.create(request);
        bedTypeEntity.addBedTypeLocaleEntity(entity);
        localeEntity.addBedTypeLocaleEntity(entity);
        bedTypeLocaleRepository.save(entity);
        log.info("BedTypeLocale created with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse update(BedTypeLocaleEntity entity,
                                  UpdateBedTypeLocaleRequest request) {
        BedTypeLocaleMapper.update(entity, request);
        bedTypeLocaleRepository.save(entity);
        log.info("BedTypeLocale updated with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse delete(BedTypeLocaleEntity entity) {
        entity.setIsDeleted(true);
        entity.setIsActive(false);
        bedTypeLocaleRepository.save(entity);
        log.info("BedTypeLocale soft-deleted with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public BedTypeLocaleEntity getEntityById(Long bedTypeId, Long id) {
        return bedTypeLocaleRepository
                .findByBedTypeEntity_IdAndIdAndIsActiveAndIsDeleted(bedTypeId, id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("BedTypeLocale not found with id: " + id));
    }

    @Override
    public PaginatedResponse<BedTypeLocaleDto> getAll(Long bedTypeId, String localeCode, PaginatedRequest paginatedRequest) {
        Pageable pageable = paginatedRequest.toPageable(Set.of());
        Page<@NonNull BedTypeLocaleDto> dtoPage = (localeCode == null || localeCode.isBlank()
                ? bedTypeLocaleRepository.findByBedTypeEntity_IdAndIsActiveAndIsDeleted(bedTypeId, true, false, pageable)
                : bedTypeLocaleRepository.findByBedTypeEntity_IdAndLocaleEntity_CodeContainingIgnoreCaseAndIsActiveAndIsDeleted(
                        bedTypeId, localeCode, true, false, pageable))
                .map(BedTypeLocaleMapper::toDto);
        return Pagination.buildPaginatedResponse(dtoPage);
    }

    @Override
    public LocaleCountResponse getCount(Long bedTypeId) {
        List<String> codes = bedTypeLocaleRepository
                .findLocaleEntity_CodeByBedTypeEntity_IdAndIsActiveAndIsDeleted(bedTypeId, true, false);
        return new LocaleCountResponse((long) codes.size(), codes);
    }
}
