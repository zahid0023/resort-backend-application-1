package com.example.resortbackendapplication1.price.serviceImpl;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.commons.utils.Pagination;
import com.example.resortbackendapplication1.price.dto.request.pricetypescope.locale.CreatePriceTypeScopeLocaleRequest;
import com.example.resortbackendapplication1.price.dto.request.pricetypescope.locale.UpdatePriceTypeScopeLocaleRequest;
import com.example.resortbackendapplication1.price.model.dto.PriceTypeScopeLocaleDto;
import com.example.resortbackendapplication1.price.model.entity.PriceTypeScopeEntity;
import com.example.resortbackendapplication1.price.model.entity.PriceTypeScopeLocaleEntity;
import com.example.resortbackendapplication1.price.model.mapper.PriceTypeScopeLocaleMapper;
import com.example.resortbackendapplication1.price.repository.PriceTypeScopeLocaleRepository;
import com.example.resortbackendapplication1.price.service.PriceTypeScopeLocaleService;
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
public class PriceTypeScopeLocaleServiceImpl implements PriceTypeScopeLocaleService {
    private final PriceTypeScopeLocaleRepository priceTypeScopeLocaleRepository;

    public PriceTypeScopeLocaleServiceImpl(PriceTypeScopeLocaleRepository priceTypeScopeLocaleRepository) {
        this.priceTypeScopeLocaleRepository = priceTypeScopeLocaleRepository;
    }

    @Transactional
    @Override
    public SuccessResponse create(CreatePriceTypeScopeLocaleRequest request,
                                  PriceTypeScopeEntity priceTypeScopeEntity,
                                  LocaleEntity localeEntity) {
        if (priceTypeScopeLocaleRepository.existsByPriceTypeScopeEntity_IdAndLocaleEntity_IdAndIsActiveAndIsDeleted(
                priceTypeScopeEntity.getId(), localeEntity.getId(), true, false)) {
            throw new IllegalStateException("PriceTypeScopeLocale already exists for priceTypeScopeId '"
                    + priceTypeScopeEntity.getId() + "' and localeId '" + localeEntity.getId() + "'");
        }

        if (priceTypeScopeLocaleRepository.existsByLocaleEntity_IdAndNameAndIsActiveAndIsDeleted(
                localeEntity.getId(), request.getName(), true, false)) {
            throw new IllegalStateException("PriceTypeScopeLocale with name '" + request.getName()
                    + "' already exists for localeId '" + localeEntity.getId() + "'");
        }

        PriceTypeScopeLocaleEntity entity = PriceTypeScopeLocaleMapper.create(request);
        priceTypeScopeEntity.addPriceTypeScopeLocaleEntity(entity);
        localeEntity.addPriceTypeScopeLocaleEntity(entity);
        priceTypeScopeLocaleRepository.save(entity);
        log.info("PriceTypeScopeLocale created with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse update(PriceTypeScopeLocaleEntity entity,
                                  UpdatePriceTypeScopeLocaleRequest request) {
        if (priceTypeScopeLocaleRepository.existsByLocaleEntity_IdAndNameAndIdNotAndIsActiveAndIsDeleted(
                entity.getLocaleEntity().getId(), request.getName(), entity.getId(), true, false)) {
            throw new IllegalStateException("PriceTypeScopeLocale with name '" + request.getName()
                    + "' already exists for localeId '" + entity.getLocaleEntity().getId() + "'");
        }

        PriceTypeScopeLocaleMapper.update(entity, request);
        priceTypeScopeLocaleRepository.save(entity);
        log.info("PriceTypeScopeLocale updated with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse delete(PriceTypeScopeLocaleEntity entity) {
        entity.setIsDeleted(true);
        entity.setIsActive(false);
        priceTypeScopeLocaleRepository.save(entity);
        log.info("PriceTypeScopeLocale soft-deleted with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public PriceTypeScopeLocaleEntity getEntityById(Long priceTypeScopeId, Long id) {
        return priceTypeScopeLocaleRepository
                .findByPriceTypeScopeEntity_IdAndIdAndIsActiveAndIsDeleted(priceTypeScopeId, id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("PriceTypeScopeLocale not found with id: " + id));
    }

    @Override
    public PaginatedResponse<PriceTypeScopeLocaleDto> getAll(Long priceTypeScopeId, String localeCode, PaginatedRequest paginatedRequest) {
        Pageable pageable = paginatedRequest.toPageable(Set.of());
        Page<@NonNull PriceTypeScopeLocaleDto> dtoPage = (localeCode == null || localeCode.isBlank()
                ? priceTypeScopeLocaleRepository.findByPriceTypeScopeEntity_IdAndIsActiveAndIsDeleted(priceTypeScopeId, true, false, pageable)
                : priceTypeScopeLocaleRepository.findByPriceTypeScopeEntity_IdAndLocaleEntity_CodeContainingIgnoreCaseAndIsActiveAndIsDeleted(
                        priceTypeScopeId, localeCode, true, false, pageable))
                .map(PriceTypeScopeLocaleMapper::toDto);
        return Pagination.buildPaginatedResponse(dtoPage);
    }

    @Override
    public LocaleCountResponse getCount(Long priceTypeScopeId) {
        List<String> codes = priceTypeScopeLocaleRepository
                .findLocaleEntity_CodeByPriceTypeScopeEntity_IdAndIsActiveAndIsDeleted(priceTypeScopeId, true, false);
        return new LocaleCountResponse((long) codes.size(), codes);
    }
}
