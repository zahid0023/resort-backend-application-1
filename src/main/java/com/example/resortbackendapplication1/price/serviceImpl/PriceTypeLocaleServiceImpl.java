package com.example.resortbackendapplication1.price.serviceImpl;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.commons.utils.Pagination;
import com.example.resortbackendapplication1.price.dto.request.pricetype.locale.CreatePriceTypeLocaleRequest;
import com.example.resortbackendapplication1.price.dto.request.pricetype.locale.UpdatePriceTypeLocaleRequest;
import com.example.resortbackendapplication1.price.model.dto.PriceTypeLocaleDto;
import com.example.resortbackendapplication1.price.model.entity.PriceTypeEntity;
import com.example.resortbackendapplication1.price.model.entity.PriceTypeLocaleEntity;
import com.example.resortbackendapplication1.price.model.mapper.PriceTypeLocaleMapper;
import com.example.resortbackendapplication1.price.repository.PriceTypeLocaleRepository;
import com.example.resortbackendapplication1.price.service.PriceTypeLocaleService;
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
public class PriceTypeLocaleServiceImpl implements PriceTypeLocaleService {
    private final PriceTypeLocaleRepository priceTypeLocaleRepository;

    public PriceTypeLocaleServiceImpl(PriceTypeLocaleRepository priceTypeLocaleRepository) {
        this.priceTypeLocaleRepository = priceTypeLocaleRepository;
    }

    @Transactional
    @Override
    public SuccessResponse create(CreatePriceTypeLocaleRequest request,
                                  PriceTypeEntity priceTypeEntity,
                                  LocaleEntity localeEntity) {
        if (priceTypeLocaleRepository.existsByPriceTypeEntity_IdAndLocaleEntity_IdAndIsActiveAndIsDeleted(
                priceTypeEntity.getId(), localeEntity.getId(), true, false)) {
            throw new IllegalStateException("PriceTypeLocale already exists for priceTypeId '"
                    + priceTypeEntity.getId() + "' and localeId '" + localeEntity.getId() + "'");
        }

        if (priceTypeLocaleRepository.existsByLocaleEntity_IdAndNameAndIsActiveAndIsDeleted(
                localeEntity.getId(), request.getName(), true, false)) {
            throw new IllegalStateException("PriceTypeLocale with name '" + request.getName()
                    + "' already exists for localeId '" + localeEntity.getId() + "'");
        }

        PriceTypeLocaleEntity entity = PriceTypeLocaleMapper.create(request);
        priceTypeEntity.addPriceTypeLocaleEntity(entity);
        localeEntity.addPriceTypeLocaleEntity(entity);
        priceTypeLocaleRepository.save(entity);
        log.info("PriceTypeLocale created with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse update(PriceTypeLocaleEntity entity,
                                  UpdatePriceTypeLocaleRequest request) {
        if (priceTypeLocaleRepository.existsByLocaleEntity_IdAndNameAndIdNotAndIsActiveAndIsDeleted(
                entity.getLocaleEntity().getId(), request.getName(), entity.getId(), true, false)) {
            throw new IllegalStateException("PriceTypeLocale with name '" + request.getName()
                    + "' already exists for localeId '" + entity.getLocaleEntity().getId() + "'");
        }

        PriceTypeLocaleMapper.update(entity, request);
        priceTypeLocaleRepository.save(entity);
        log.info("PriceTypeLocale updated with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse delete(PriceTypeLocaleEntity entity) {
        entity.setIsDeleted(true);
        entity.setIsActive(false);
        priceTypeLocaleRepository.save(entity);
        log.info("PriceTypeLocale soft-deleted with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public PriceTypeLocaleEntity getEntityById(Long priceTypeId, Long id) {
        return priceTypeLocaleRepository
                .findByPriceTypeEntity_IdAndIdAndIsActiveAndIsDeleted(priceTypeId, id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("PriceTypeLocale not found with id: " + id));
    }

    @Override
    public PaginatedResponse<PriceTypeLocaleDto> getAll(Long priceTypeId, String localeCode, PaginatedRequest paginatedRequest) {
        Pageable pageable = paginatedRequest.toPageable(Set.of());
        Page<@NonNull PriceTypeLocaleDto> dtoPage = (localeCode == null || localeCode.isBlank()
                ? priceTypeLocaleRepository.findByPriceTypeEntity_IdAndIsActiveAndIsDeleted(priceTypeId, true, false, pageable)
                : priceTypeLocaleRepository.findByPriceTypeEntity_IdAndLocaleEntity_CodeContainingIgnoreCaseAndIsActiveAndIsDeleted(
                        priceTypeId, localeCode, true, false, pageable))
                .map(PriceTypeLocaleMapper::toDto);
        return Pagination.buildPaginatedResponse(dtoPage);
    }

    @Override
    public LocaleCountResponse getCount(Long priceTypeId) {
        List<String> codes = priceTypeLocaleRepository
                .findLocaleEntity_CodeByPriceTypeEntity_IdAndIsActiveAndIsDeleted(priceTypeId, true, false);
        return new LocaleCountResponse((long) codes.size(), codes);
    }
}
