package com.example.resortbackendapplication1.resortroomcategoryprice.serviceImpl;

import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.commons.utils.Pagination;
import com.example.resortbackendapplication1.currency.model.entity.CurrencyEntity;
import com.example.resortbackendapplication1.dayofweek.model.entity.DayOfWeekEntity;
import com.example.resortbackendapplication1.price.model.entity.PriceTypeEntity;
import com.example.resortbackendapplication1.price.model.entity.PriceUnitEntity;
import com.example.resortbackendapplication1.resortroomcategory.model.entity.ResortRoomCategoryEntity;
import com.example.resortbackendapplication1.resortroomcategoryprice.dto.request.CreateResortRoomCategoryPriceRequest;
import com.example.resortbackendapplication1.resortroomcategoryprice.dto.request.ResortRoomCategoryPriceFilterRequest;
import com.example.resortbackendapplication1.resortroomcategoryprice.dto.request.UpdateResortRoomCategoryPriceRequest;
import com.example.resortbackendapplication1.resortroomcategoryprice.dto.response.ResortRoomCategoryPriceResponse;
import com.example.resortbackendapplication1.resortroomcategoryprice.model.dto.ResortRoomCategoryPriceDto;
import com.example.resortbackendapplication1.resortroomcategoryprice.model.entity.ResortRoomCategoryPriceEntity;
import com.example.resortbackendapplication1.resortroomcategoryprice.model.enums.PriceTypeCode;
import com.example.resortbackendapplication1.resortroomcategoryprice.model.enums.ResortRoomCategoryPriceSortField;
import com.example.resortbackendapplication1.resortroomcategoryprice.model.mapper.ResortRoomCategoryPriceMapper;
import com.example.resortbackendapplication1.resortroomcategoryprice.repository.ResortRoomCategoryPriceRepository;
import com.example.resortbackendapplication1.resortroomcategoryprice.service.ResortRoomCategoryPriceService;
import com.example.resortbackendapplication1.resortroomcategoryprice.specification.ResortRoomCategoryPriceSpecification;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@Slf4j
public class ResortRoomCategoryPriceServiceImpl implements ResortRoomCategoryPriceService {

    private static final Set<String> ALLOWED_SORT_FIELDS = ResortRoomCategoryPriceSortField.allowedFields();
    private static final Set<String> ALLOWED_SEARCH_FIELDS = Set.of();

    private final ResortRoomCategoryPriceRepository resortRoomCategoryPriceRepository;

    public ResortRoomCategoryPriceServiceImpl(ResortRoomCategoryPriceRepository resortRoomCategoryPriceRepository) {
        this.resortRoomCategoryPriceRepository = resortRoomCategoryPriceRepository;
    }

    @Transactional
    @Override
    public SuccessResponse create(CreateResortRoomCategoryPriceRequest request,
                                  ResortRoomCategoryEntity resortRoomCategoryEntity,
                                  PriceTypeEntity priceTypeEntity,
                                  PriceUnitEntity priceUnitEntity,
                                  CurrencyEntity currencyEntity,
                                  List<DayOfWeekEntity> dayEntities) {
        PriceTypeCode typeCode = PriceTypeCode.fromCode(priceTypeEntity.getCode());
        validateTypeRules(typeCode, request.getDayOfWeekIds(), request.getValidFrom(), request.getValidTo());
        validatePrerequisites(typeCode, resortRoomCategoryEntity.getId(), currencyEntity.getId());

        if (typeCode.hasFixedPriority()) {
            validateSingletonPrice(resortRoomCategoryEntity.getId(), priceTypeEntity.getId(), currencyEntity.getId(), null);
        }

        int priority = resolvePriority(typeCode, request.getPriority());

        if (typeCode == PriceTypeCode.WKD || typeCode == PriceTypeCode.WKE) {
            validatePriceNotAboveBase(request.getPrice(), resortRoomCategoryEntity.getId(), currencyEntity.getId());
        }

        ResortRoomCategoryPriceEntity entity = ResortRoomCategoryPriceMapper.create(
                request, resortRoomCategoryEntity, priceTypeEntity, priceUnitEntity,
                currencyEntity, priority, dayEntities);
        resortRoomCategoryPriceRepository.save(entity);
        log.info("ResortRoomCategoryPrice created with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public ResortRoomCategoryPriceEntity getEntityById(Long resortRoomCategoryId, Long id) {
        return resortRoomCategoryPriceRepository
                .findByIdAndResortRoomCategoryEntity_IdAndIsActiveAndIsDeleted(id, resortRoomCategoryId, true, false)
                .orElseThrow(() -> new EntityNotFoundException("ResortRoomCategoryPrice not found with id: " + id));
    }

    @Override
    public ResortRoomCategoryPriceResponse getById(Long resortRoomCategoryId, Long id) {
        ResortRoomCategoryPriceEntity entity = getEntityById(resortRoomCategoryId, id);
        BigDecimal basePrice = findBasePriceAmount(resortRoomCategoryId, entity.getCurrencyEntity().getId());
        return new ResortRoomCategoryPriceResponse(ResortRoomCategoryPriceMapper.toDto(entity, basePrice));
    }

    @Override
    public PaginatedResponse<ResortRoomCategoryPriceDto> getAll(ResortRoomCategoryPriceFilterRequest request,
                                                                Long resortRoomCategoryId) {
        Page<@NonNull ResortRoomCategoryPriceEntity> entityPage = resortRoomCategoryPriceRepository
                .findAll(ResortRoomCategoryPriceSpecification.filter(request, resortRoomCategoryId),
                        request.toPageable(ALLOWED_SORT_FIELDS));

        Map<Long, BigDecimal> basePriceCache = new HashMap<>();
        entityPage.getContent().stream()
                .map(e -> e.getCurrencyEntity().getId())
                .distinct()
                .forEach(currencyId -> basePriceCache.put(
                        currencyId, findBasePriceAmount(resortRoomCategoryId, currencyId)));

        Page<@NonNull ResortRoomCategoryPriceDto> page = entityPage.map(entity -> {
            BigDecimal basePrice = basePriceCache.get(entity.getCurrencyEntity().getId());
            return ResortRoomCategoryPriceMapper.toDto(entity, basePrice);
        });
        return Pagination.buildPaginatedResponse(page, ALLOWED_SORT_FIELDS, ALLOWED_SEARCH_FIELDS);
    }

    @Transactional
    @Override
    public SuccessResponse update(ResortRoomCategoryPriceEntity entity,
                                  UpdateResortRoomCategoryPriceRequest request,
                                  PriceUnitEntity priceUnitEntity,
                                  List<DayOfWeekEntity> dayEntities) {
        PriceTypeCode typeCode = PriceTypeCode.fromCode(entity.getPriceTypeEntity().getCode());
        validateTypeRules(typeCode, request.getDayOfWeekIds(), request.getValidFrom(), request.getValidTo());

        int priority = resolvePriority(typeCode, request.getPriority());

        if (typeCode == PriceTypeCode.WKD || typeCode == PriceTypeCode.WKE) {
            validatePriceNotAboveBase(request.getPrice(),
                    entity.getResortRoomCategoryEntity().getId(),
                    entity.getCurrencyEntity().getId());
        }

        ResortRoomCategoryPriceMapper.update(entity, request, priceUnitEntity, priority, dayEntities);
        resortRoomCategoryPriceRepository.save(entity);
        log.info("ResortRoomCategoryPrice updated with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse delete(ResortRoomCategoryPriceEntity entity) {
        entity.setIsDeleted(true);
        entity.setIsActive(false);
        resortRoomCategoryPriceRepository.save(entity);
        log.info("ResortRoomCategoryPrice soft-deleted with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    private void validatePrerequisites(PriceTypeCode typeCode, Long categoryId, Long currencyId) {
        switch (typeCode) {
            case WKD -> {
                if (!resortRoomCategoryPriceRepository.existsByTypeCode(categoryId, currencyId, "BAS")) {
                    throw new IllegalStateException(
                            "A Base price must exist for this room category and currency before creating a Weekday price.");
                }
            }
            case WKE -> {
                if (!resortRoomCategoryPriceRepository.existsByTypeCode(categoryId, currencyId, "BAS")) {
                    throw new IllegalStateException(
                            "A Base price must exist for this room category and currency before creating a Weekend price.");
                }
                if (!resortRoomCategoryPriceRepository.existsByTypeCode(categoryId, currencyId, "WKD")) {
                    throw new IllegalStateException(
                            "A Weekday price must exist for this room category and currency before creating a Weekend price.");
                }
            }
            case HOL, SPE -> {
                long foundationCount = resortRoomCategoryPriceRepository.countFoundationPrices(categoryId, currencyId);
                if (foundationCount < 3) {
                    throw new IllegalStateException(
                            "Base, Weekday, and Weekend prices must all exist for this room category and currency before creating a Holiday or Special price.");
                }
            }
            default -> { /* BAS has no prerequisites */ }
        }
    }

    private void validateTypeRules(PriceTypeCode typeCode, Set<Long> dayOfWeekIds,
                                   LocalDate validFrom, LocalDate validTo) {
        if (typeCode.requiresDays()) {
            if (dayOfWeekIds == null || dayOfWeekIds.isEmpty()) {
                throw new IllegalArgumentException(
                        "day_of_week_ids is required for price type " + typeCode);
            }
            if (validFrom != null || validTo != null) {
                throw new IllegalArgumentException(
                        "Date range (valid_from / valid_to) is not allowed for price type " + typeCode);
            }
        } else if (typeCode.requiresDateRange()) {
            if (validFrom == null || validTo == null) {
                throw new IllegalArgumentException(
                        "valid_from and valid_to are required for price type " + typeCode);
            }
            if (validFrom.isAfter(validTo)) {
                throw new IllegalArgumentException("valid_from must not be after valid_to");
            }
        } else {
            // BAS: no days, no date range
            if (dayOfWeekIds != null && !dayOfWeekIds.isEmpty()) {
                throw new IllegalArgumentException(
                        "day_of_week_ids is not allowed for price type " + typeCode);
            }
            if (validFrom != null || validTo != null) {
                throw new IllegalArgumentException(
                        "Date range (valid_from / valid_to) is not allowed for price type " + typeCode);
            }
        }
    }

    private void validateSingletonPrice(Long categoryId, Long priceTypeId, Long currencyId, Long excludeId) {
        boolean exists = excludeId == null
                ? resortRoomCategoryPriceRepository
                .existsByResortRoomCategoryEntity_IdAndPriceTypeEntity_IdAndCurrencyEntity_IdAndIsDeletedFalse(
                        categoryId, priceTypeId, currencyId)
                : resortRoomCategoryPriceRepository
                .existsByResortRoomCategoryEntity_IdAndPriceTypeEntity_IdAndCurrencyEntity_IdAndIsDeletedFalseAndIdNot(
                        categoryId, priceTypeId, currencyId, excludeId);
        if (exists) {
            throw new IllegalStateException(
                    "A price of this type already exists for this room category and currency.");
        }
    }

    private int resolvePriority(PriceTypeCode typeCode, Integer requestedPriority) {
        if (typeCode.hasFixedPriority()) {
            return typeCode.getDefaultPriority();
        }
        if (requestedPriority == null) {
            return typeCode.getDefaultPriority();
        }
        if (requestedPriority < typeCode.getDefaultPriority()) {
            throw new IllegalArgumentException(
                    "Priority for " + typeCode + " must be at least " + typeCode.getDefaultPriority());
        }
        return requestedPriority;
    }

    private void validatePriceNotAboveBase(BigDecimal price, Long categoryId, Long currencyId) {
        resortRoomCategoryPriceRepository.findBasePrice(categoryId, currencyId).ifPresent(base -> {
            if (price.compareTo(base.getPrice()) > 0) {
                throw new IllegalArgumentException(
                        "Weekday/Weekend price (" + price + ") must not exceed the base price (" +
                                base.getPrice() + ")");
            }
        });
    }

    private BigDecimal findBasePriceAmount(Long categoryId, Long currencyId) {
        return resortRoomCategoryPriceRepository.findBasePrice(categoryId, currencyId)
                .map(ResortRoomCategoryPriceEntity::getPrice)
                .orElse(null);
    }
}
