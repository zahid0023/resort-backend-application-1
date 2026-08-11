package com.example.resortbackendapplication1.price.serviceImpl;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.commons.utils.Pagination;
import com.example.resortbackendapplication1.price.dto.request.pricescope.locale.CreatePriceScopeLocaleRequest;
import com.example.resortbackendapplication1.price.dto.request.pricescope.locale.UpdatePriceScopeLocaleRequest;
import com.example.resortbackendapplication1.price.model.dto.PriceScopeLocaleDto;
import com.example.resortbackendapplication1.price.model.entity.PriceScopeEntity;
import com.example.resortbackendapplication1.price.model.entity.PriceScopeLocaleEntity;
import com.example.resortbackendapplication1.price.model.mapper.PriceScopeLocaleMapper;
import com.example.resortbackendapplication1.price.repository.PriceScopeLocaleRepository;
import com.example.resortbackendapplication1.price.service.PriceScopeLocaleService;
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
public class PriceScopeLocaleServiceImpl implements PriceScopeLocaleService {
    private final PriceScopeLocaleRepository priceScopeLocaleRepository;

    public PriceScopeLocaleServiceImpl(PriceScopeLocaleRepository priceScopeLocaleRepository) {
        this.priceScopeLocaleRepository = priceScopeLocaleRepository;
    }

    @Transactional
    @Override
    public SuccessResponse create(CreatePriceScopeLocaleRequest request,
                                  PriceScopeEntity priceScopeEntity,
                                  LocaleEntity localeEntity) {
        if (priceScopeLocaleRepository.existsByPriceScopeEntity_IdAndLocaleEntity_IdAndIsActiveAndIsDeleted(
                priceScopeEntity.getId(), localeEntity.getId(), true, false)) {
            throw new IllegalStateException("PriceScopeLocale already exists for priceScopeId '"
                    + priceScopeEntity.getId() + "' and localeId '" + localeEntity.getId() + "'");
        }

        if (priceScopeLocaleRepository.existsByLocaleEntity_IdAndNameAndIsActiveAndIsDeleted(
                localeEntity.getId(), request.getName(), true, false)) {
            throw new IllegalStateException("PriceScopeLocale with name '" + request.getName()
                    + "' already exists for localeId '" + localeEntity.getId() + "'");
        }

        PriceScopeLocaleEntity entity = PriceScopeLocaleMapper.create(request);
        priceScopeEntity.addPriceScopeLocaleEntity(entity);
        localeEntity.addPriceScopeLocaleEntity(entity);
        priceScopeLocaleRepository.save(entity);
        log.info("PriceScopeLocale created with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse update(PriceScopeLocaleEntity entity,
                                  UpdatePriceScopeLocaleRequest request) {
        if (priceScopeLocaleRepository.existsByLocaleEntity_IdAndNameAndIdNotAndIsActiveAndIsDeleted(
                entity.getLocaleEntity().getId(), request.getName(), entity.getId(), true, false)) {
            throw new IllegalStateException("PriceScopeLocale with name '" + request.getName()
                    + "' already exists for localeId '" + entity.getLocaleEntity().getId() + "'");
        }

        PriceScopeLocaleMapper.update(entity, request);
        priceScopeLocaleRepository.save(entity);
        log.info("PriceScopeLocale updated with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse delete(PriceScopeLocaleEntity entity) {
        entity.setIsDeleted(true);
        entity.setIsActive(false);
        priceScopeLocaleRepository.save(entity);
        log.info("PriceScopeLocale soft-deleted with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public PriceScopeLocaleEntity getEntityById(Long priceScopeId, Long id) {
        return priceScopeLocaleRepository
                .findByPriceScopeEntity_IdAndIdAndIsActiveAndIsDeleted(priceScopeId, id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("PriceScopeLocale not found with id: " + id));
    }

    @Override
    public PaginatedResponse<PriceScopeLocaleDto> getAll(Long priceScopeId, String localeCode, PaginatedRequest paginatedRequest) {
        Pageable pageable = paginatedRequest.toPageable(Set.of());
        Page<@NonNull PriceScopeLocaleDto> dtoPage = (localeCode == null || localeCode.isBlank()
                ? priceScopeLocaleRepository.findByPriceScopeEntity_IdAndIsActiveAndIsDeleted(priceScopeId, true, false, pageable)
                : priceScopeLocaleRepository.findByPriceScopeEntity_IdAndLocaleEntity_CodeContainingIgnoreCaseAndIsActiveAndIsDeleted(
                        priceScopeId, localeCode, true, false, pageable))
                .map(PriceScopeLocaleMapper::toDto);
        return Pagination.buildPaginatedResponse(dtoPage);
    }

    @Override
    public LocaleCountResponse getCount(Long priceScopeId) {
        List<String> codes = priceScopeLocaleRepository
                .findLocaleEntity_CodeByPriceScopeEntity_IdAndIsActiveAndIsDeleted(priceScopeId, true, false);
        return new LocaleCountResponse((long) codes.size(), codes);
    }
}
