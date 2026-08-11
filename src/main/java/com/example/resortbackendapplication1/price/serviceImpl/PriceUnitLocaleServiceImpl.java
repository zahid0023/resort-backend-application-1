package com.example.resortbackendapplication1.price.serviceImpl;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.commons.utils.Pagination;
import com.example.resortbackendapplication1.price.dto.request.priceunit.locale.CreatePriceUnitLocaleRequest;
import com.example.resortbackendapplication1.price.dto.request.priceunit.locale.UpdatePriceUnitLocaleRequest;
import com.example.resortbackendapplication1.price.model.dto.PriceUnitLocaleDto;
import com.example.resortbackendapplication1.price.model.entity.PriceUnitEntity;
import com.example.resortbackendapplication1.price.model.entity.PriceUnitLocaleEntity;
import com.example.resortbackendapplication1.price.model.mapper.PriceUnitLocaleMapper;
import com.example.resortbackendapplication1.price.repository.PriceUnitLocaleRepository;
import com.example.resortbackendapplication1.price.service.PriceUnitLocaleService;
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
public class PriceUnitLocaleServiceImpl implements PriceUnitLocaleService {
    private final PriceUnitLocaleRepository priceUnitLocaleRepository;

    public PriceUnitLocaleServiceImpl(PriceUnitLocaleRepository priceUnitLocaleRepository) {
        this.priceUnitLocaleRepository = priceUnitLocaleRepository;
    }

    @Transactional
    @Override
    public SuccessResponse create(CreatePriceUnitLocaleRequest request,
                                  PriceUnitEntity priceUnitEntity,
                                  LocaleEntity localeEntity) {
        if (priceUnitLocaleRepository.existsByPriceUnitEntity_IdAndLocaleEntity_IdAndIsActiveAndIsDeleted(
                priceUnitEntity.getId(), localeEntity.getId(), true, false)) {
            throw new IllegalStateException("PriceUnitLocale already exists for priceUnitId '"
                    + priceUnitEntity.getId() + "' and localeId '" + localeEntity.getId() + "'");
        }

        if (priceUnitLocaleRepository.existsByLocaleEntity_IdAndNameAndIsActiveAndIsDeleted(
                localeEntity.getId(), request.getName(), true, false)) {
            throw new IllegalStateException("PriceUnitLocale with name '" + request.getName()
                    + "' already exists for localeId '" + localeEntity.getId() + "'");
        }

        PriceUnitLocaleEntity entity = PriceUnitLocaleMapper.create(request);
        priceUnitEntity.addPriceUnitLocaleEntity(entity);
        localeEntity.addPriceUnitLocaleEntity(entity);
        priceUnitLocaleRepository.save(entity);
        log.info("PriceUnitLocale created with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse update(PriceUnitLocaleEntity entity,
                                  UpdatePriceUnitLocaleRequest request) {
        if (priceUnitLocaleRepository.existsByLocaleEntity_IdAndNameAndIdNotAndIsActiveAndIsDeleted(
                entity.getLocaleEntity().getId(), request.getName(), entity.getId(), true, false)) {
            throw new IllegalStateException("PriceUnitLocale with name '" + request.getName()
                    + "' already exists for localeId '" + entity.getLocaleEntity().getId() + "'");
        }

        PriceUnitLocaleMapper.update(entity, request);
        priceUnitLocaleRepository.save(entity);
        log.info("PriceUnitLocale updated with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse delete(PriceUnitLocaleEntity entity) {
        entity.setIsDeleted(true);
        entity.setIsActive(false);
        priceUnitLocaleRepository.save(entity);
        log.info("PriceUnitLocale soft-deleted with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public PriceUnitLocaleEntity getEntityById(Long priceUnitId, Long id) {
        return priceUnitLocaleRepository
                .findByPriceUnitEntity_IdAndIdAndIsActiveAndIsDeleted(priceUnitId, id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("PriceUnitLocale not found with id: " + id));
    }

    @Override
    public PaginatedResponse<PriceUnitLocaleDto> getAll(Long priceUnitId, String localeCode, PaginatedRequest paginatedRequest) {
        Pageable pageable = paginatedRequest.toPageable(Set.of());
        Page<@NonNull PriceUnitLocaleDto> dtoPage = (localeCode == null || localeCode.isBlank()
                ? priceUnitLocaleRepository.findByPriceUnitEntity_IdAndIsActiveAndIsDeleted(priceUnitId, true, false, pageable)
                : priceUnitLocaleRepository.findByPriceUnitEntity_IdAndLocaleEntity_CodeContainingIgnoreCaseAndIsActiveAndIsDeleted(
                        priceUnitId, localeCode, true, false, pageable))
                .map(PriceUnitLocaleMapper::toDto);
        return Pagination.buildPaginatedResponse(dtoPage);
    }

    @Override
    public LocaleCountResponse getCount(Long priceUnitId) {
        List<String> codes = priceUnitLocaleRepository
                .findLocaleEntity_CodeByPriceUnitEntity_IdAndIsActiveAndIsDeleted(priceUnitId, true, false);
        return new LocaleCountResponse((long) codes.size(), codes);
    }
}
