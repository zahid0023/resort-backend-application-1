package com.example.resortbackendapplication1.resort.serviceImpl;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.commons.utils.Pagination;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.resort.dto.request.resortaddress.locale.CreateResortAddressLocaleRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortaddress.locale.UpdateResortAddressLocaleRequest;
import com.example.resortbackendapplication1.resort.dto.response.resortaddresslocales.ResortAddressLocaleCountResponse;
import com.example.resortbackendapplication1.resort.model.dto.ResortAddressLocaleDto;
import com.example.resortbackendapplication1.resort.model.entity.ResortAddressEntity;
import com.example.resortbackendapplication1.resort.model.entity.ResortAddressLocaleEntity;
import com.example.resortbackendapplication1.resort.model.mapper.ResortAddressLocaleMapper;
import com.example.resortbackendapplication1.resort.repository.ResortAddressLocaleRepository;
import com.example.resortbackendapplication1.resort.service.ResortAddressLocaleService;
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
public class ResortAddressLocaleServiceImpl implements ResortAddressLocaleService {

    private final ResortAddressLocaleRepository resortAddressLocaleRepository;

    public ResortAddressLocaleServiceImpl(ResortAddressLocaleRepository resortAddressLocaleRepository) {
        this.resortAddressLocaleRepository = resortAddressLocaleRepository;
    }

    @Transactional
    @Override
    public SuccessResponse create(CreateResortAddressLocaleRequest request,
                                  ResortAddressEntity resortAddressEntity,
                                  LocaleEntity localeEntity) {
        if (resortAddressLocaleRepository.existsByResortAddressEntity_IdAndLocaleEntity_IdAndIsActiveAndIsDeleted(
                resortAddressEntity.getId(), localeEntity.getId(), true, false)) {
            throw new IllegalStateException("ResortAddress already has a locale entry for locale id: " + localeEntity.getId());
        }

        ResortAddressLocaleEntity entity = ResortAddressLocaleMapper.create(request);
        resortAddressEntity.addResortAddressLocaleEntity(entity);
        localeEntity.addResortAddressLocaleEntity(entity);
        resortAddressLocaleRepository.save(entity);
        log.info("ResortAddressLocale created with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public ResortAddressLocaleEntity getEntityById(Long resortAddressId, Long id) {
        return resortAddressLocaleRepository
                .findByResortAddressEntity_IdAndIdAndIsActiveAndIsDeleted(resortAddressId, id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("ResortAddressLocale not found with id: " + id));
    }

    @Override
    public PaginatedResponse<ResortAddressLocaleDto> getAll(Long resortAddressId, String localeCode, PaginatedRequest paginatedRequest) {
        Pageable pageable = paginatedRequest.toPageable(Set.of());
        Page<@NonNull ResortAddressLocaleDto> dtoPage = (localeCode == null || localeCode.isBlank()
                ? resortAddressLocaleRepository.findByResortAddressEntity_IdAndIsActiveAndIsDeleted(resortAddressId, true, false, pageable)
                : resortAddressLocaleRepository.findByResortAddressEntity_IdAndLocaleEntity_CodeContainingIgnoreCaseAndIsActiveAndIsDeleted(
                        resortAddressId, localeCode, true, false, pageable))
                .map(ResortAddressLocaleMapper::toDto);
        return Pagination.buildPaginatedResponse(dtoPage);
    }

    @Override
    public ResortAddressLocaleCountResponse getActiveCount(Long resortAddressId) {
        List<String> codes = resortAddressLocaleRepository
                .findLocaleCodeByResortAddressEntity_IdAndIsActiveAndIsDeleted(resortAddressId, true, false);
        return new ResortAddressLocaleCountResponse((long) codes.size(), codes);
    }

    @Transactional
    @Override
    public SuccessResponse update(ResortAddressLocaleEntity entity, UpdateResortAddressLocaleRequest request) {
        ResortAddressLocaleMapper.update(entity, request);
        resortAddressLocaleRepository.save(entity);
        log.info("ResortAddressLocale updated with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse delete(ResortAddressLocaleEntity entity) {
        entity.setIsDeleted(true);
        entity.setIsActive(false);
        resortAddressLocaleRepository.save(entity);
        log.info("ResortAddressLocale soft-deleted with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }
}
