package com.example.resortbackendapplication1.resort.serviceImpl;

import com.example.resortbackendapplication1.bedtype.model.entity.BedTypeEntity;
import com.example.resortbackendapplication1.bedtype.model.mapper.BedTypeMapper;
import com.example.resortbackendapplication1.commons.context.LocaleContext;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.commons.utils.Pagination;
import com.example.resortbackendapplication1.currency.model.entity.CurrencyEntity;
import com.example.resortbackendapplication1.dayofweek.model.entity.DayOfWeekEntity;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.price.model.entity.PriceTypeEntity;
import com.example.resortbackendapplication1.price.model.entity.PriceUnitEntity;
import com.example.resortbackendapplication1.resort.dto.request.resortroomcategory.CreateResortRoomCategoryRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortroomcategory.ResortRoomCategoryFilterRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortroomcategory.UpdateResortRoomCategoryRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortroomcategorybed.CreateResortRoomCategoryBedRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortroomcategoryprice.CreateResortRoomCategoryMainPriceRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortroomcategoryprice.ResortRoomCategoryPriceRequest;
import com.example.resortbackendapplication1.resort.dto.response.resortroomcategories.ResortRoomCategoryCountResponse;
import com.example.resortbackendapplication1.resort.dto.response.resortroomcategories.ResortRoomCategoryResponse;
import com.example.resortbackendapplication1.resort.model.dto.ResortRoomCategoryBedDto;
import com.example.resortbackendapplication1.resort.model.dto.ResortRoomCategoryDto;
import com.example.resortbackendapplication1.resort.model.entity.ResortEntity;
import com.example.resortbackendapplication1.resort.model.entity.ResortRoomCategoryBedEntity;
import com.example.resortbackendapplication1.resort.model.entity.ResortRoomCategoryEntity;
import com.example.resortbackendapplication1.resort.model.entity.ResortRoomCategoryLocaleEntity;
import com.example.resortbackendapplication1.resort.model.entity.ResortRoomCategoryMetaEntity;
import com.example.resortbackendapplication1.resort.model.entity.ResortRoomCategoryPriceDayEntity;
import com.example.resortbackendapplication1.resort.model.entity.ResortRoomCategoryPriceEntity;
import com.example.resortbackendapplication1.resort.model.enums.ResortRoomCategorySearchField;
import com.example.resortbackendapplication1.resort.model.enums.ResortRoomCategorySortField;
import com.example.resortbackendapplication1.resort.model.mapper.ResortMapper;
import com.example.resortbackendapplication1.resort.model.mapper.ResortRoomCategoryBedMapper;
import com.example.resortbackendapplication1.resort.model.mapper.ResortRoomCategoryLocaleMapper;
import com.example.resortbackendapplication1.resort.model.mapper.ResortRoomCategoryMapper;
import com.example.resortbackendapplication1.resort.model.mapper.ResortRoomCategoryMetaMapper;
import com.example.resortbackendapplication1.resort.model.mapper.ResortRoomCategoryPriceDayMapper;
import com.example.resortbackendapplication1.resort.model.mapper.ResortRoomCategoryPriceMapper;
import com.example.resortbackendapplication1.resort.repository.ResortRoomCategoryRepository;
import com.example.resortbackendapplication1.resort.service.ResortRoomCategoryMetaService;
import com.example.resortbackendapplication1.resort.service.ResortRoomCategoryService;
import com.example.resortbackendapplication1.resort.specification.ResortRoomCategorySpecification;
import com.example.resortbackendapplication1.roomcategory.model.entity.RoomCategoryEntity;
import com.example.resortbackendapplication1.roomcategory.model.mapper.RoomCategoryMapper;
import com.example.resortbackendapplication1.unit.model.entity.UnitEntity;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ResortRoomCategoryServiceImpl implements ResortRoomCategoryService {

    private static final Set<String> ALLOWED_SORT_FIELDS = ResortRoomCategorySortField.allowedFields();
    private static final Set<String> ALLOWED_SEARCH_FIELDS = ResortRoomCategorySearchField.allowedFields();

    private final ResortRoomCategoryRepository resortRoomCategoryRepository;
    private final ResortRoomCategoryMetaService resortRoomCategoryMetaService;

    public ResortRoomCategoryServiceImpl(ResortRoomCategoryRepository resortRoomCategoryRepository,
                                         ResortRoomCategoryMetaService resortRoomCategoryMetaService) {
        this.resortRoomCategoryRepository = resortRoomCategoryRepository;
        this.resortRoomCategoryMetaService = resortRoomCategoryMetaService;
    }

    @Transactional
    @Override
    public SuccessResponse create(CreateResortRoomCategoryRequest request,
                                  ResortEntity resortEntity,
                                  RoomCategoryEntity roomCategoryEntity,
                                  LocaleEntity localeEntity,
                                  UnitEntity roomSizeUnitEntity,
                                  List<BedTypeEntity> bedTypeEntities,
                                  PriceTypeEntity basePriceTypeEntity,
                                  PriceTypeEntity weekdayPriceTypeEntity,
                                  PriceTypeEntity weekendPriceTypeEntity,
                                  List<CurrencyEntity> currencyEntities,
                                  List<PriceUnitEntity> priceUnitEntities,
                                  List<DayOfWeekEntity> dayOfWeekEntities) {
        if (resortRoomCategoryRepository.existsByResortEntity_IdAndCodeAndIsActiveAndIsDeleted(
                resortEntity.getId(), request.getCode(), true, false)) {
            throw new IllegalStateException("Resort already has a room category with code: " + request.getCode());
        }
        if (resortRoomCategoryRepository.existsByResortEntity_IdAndRoomCategoryEntity_IdAndIsActiveAndIsDeleted(
                resortEntity.getId(), roomCategoryEntity.getId(), true, false)) {
            throw new IllegalStateException("Resort already has this room category linked, room category id: "
                    + roomCategoryEntity.getId());
        }

        List<CreateResortRoomCategoryBedRequest> bedRequests = request.getBeds();
        Set<Long> requestedBedTypeIds = bedRequests.stream()
                .map(CreateResortRoomCategoryBedRequest::getBedTypeId)
                .collect(Collectors.toSet());
        if (requestedBedTypeIds.size() != bedRequests.size()) {
            throw new IllegalStateException("Duplicate bed type ids are not allowed in the same request");
        }
        Map<Long, BedTypeEntity> bedTypesById = bedTypeEntities.stream()
                .collect(Collectors.toMap(BedTypeEntity::getId, bedTypeEntity -> bedTypeEntity));

        List<CreateResortRoomCategoryMainPriceRequest> priceGroupRequests = request.getPrices();
        Set<Long> requestedCurrencyIds = priceGroupRequests.stream()
                .map(CreateResortRoomCategoryMainPriceRequest::getCurrencyId)
                .collect(Collectors.toSet());
        if (requestedCurrencyIds.size() != priceGroupRequests.size()) {
            throw new IllegalStateException("Duplicate currency ids are not allowed in the same request");
        }
        Map<Long, CurrencyEntity> currenciesById = currencyEntities.stream()
                .collect(Collectors.toMap(CurrencyEntity::getId, currencyEntity -> currencyEntity));
        Map<Long, PriceUnitEntity> priceUnitsById = priceUnitEntities.stream()
                .collect(Collectors.toMap(PriceUnitEntity::getId, priceUnitEntity -> priceUnitEntity));
        Map<Long, DayOfWeekEntity> daysOfWeekById = dayOfWeekEntities.stream()
                .collect(Collectors.toMap(DayOfWeekEntity::getId, dayOfWeekEntity -> dayOfWeekEntity));

        ResortRoomCategoryEntity entity = ResortRoomCategoryMapper.create(request);
        resortEntity.addResortRoomCategoryEntity(entity);
        roomCategoryEntity.addResortRoomCategoryEntity(entity);

        ResortRoomCategoryLocaleEntity resortRoomCategoryLocaleEntity = ResortRoomCategoryLocaleMapper.create(request.getLocale());
        entity.addResortRoomCategoryLocaleEntity(resortRoomCategoryLocaleEntity);
        localeEntity.addResortRoomCategoryLocaleEntity(resortRoomCategoryLocaleEntity);

        ResortRoomCategoryMetaEntity resortRoomCategoryMetaEntity = ResortRoomCategoryMetaMapper.create(request.getMeta(), roomSizeUnitEntity);
        entity.assignResortRoomCategoryMetaEntity(resortRoomCategoryMetaEntity);

        for (CreateResortRoomCategoryBedRequest bedRequest : bedRequests) {
            BedTypeEntity bedTypeEntity = bedTypesById.get(bedRequest.getBedTypeId());
            ResortRoomCategoryBedEntity resortRoomCategoryBedEntity = ResortRoomCategoryBedMapper.create(bedRequest);
            entity.addResortRoomCategoryBedEntity(resortRoomCategoryBedEntity);
            bedTypeEntity.addResortRoomCategoryBedEntity(resortRoomCategoryBedEntity);
        }

        for (CreateResortRoomCategoryMainPriceRequest priceGroup : priceGroupRequests) {
            attachPriceGroup(entity, priceGroup, basePriceTypeEntity, weekdayPriceTypeEntity, weekendPriceTypeEntity,
                    currenciesById, priceUnitsById, daysOfWeekById);
        }

        resortRoomCategoryRepository.save(entity);
        log.info("ResortRoomCategory created with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    private void attachPriceGroup(ResortRoomCategoryEntity entity,
                                  CreateResortRoomCategoryMainPriceRequest priceGroup,
                                  PriceTypeEntity basePriceTypeEntity,
                                  PriceTypeEntity weekdayPriceTypeEntity,
                                  PriceTypeEntity weekendPriceTypeEntity,
                                  Map<Long, CurrencyEntity> currenciesById,
                                  Map<Long, PriceUnitEntity> priceUnitsById,
                                  Map<Long, DayOfWeekEntity> daysOfWeekById) {
        BigDecimal basePrice = priceGroup.getBasePriceRequest().getPrice();
        validateNotExceedingBase("WKD", priceGroup.getWeekdayPrice().getPrice(), basePrice, priceGroup.getCurrencyId());
        validateNotExceedingBase("WKE", priceGroup.getWeekendPrice().getPrice(), basePrice, priceGroup.getCurrencyId());

        CurrencyEntity currencyEntity = currenciesById.get(priceGroup.getCurrencyId());

        attachMainPriceRow(entity, priceGroup.getBasePriceRequest(), basePriceTypeEntity, currencyEntity, priceUnitsById);

        ResortRoomCategoryPriceEntity weekdayEntity = attachMainPriceRow(
                entity, priceGroup.getWeekdayPrice(), weekdayPriceTypeEntity, currencyEntity, priceUnitsById);
        attachPriceDays(weekdayEntity, priceGroup.getWeekdayPrice().getDayOfWeekIds(), daysOfWeekById);

        ResortRoomCategoryPriceEntity weekendEntity = attachMainPriceRow(
                entity, priceGroup.getWeekendPrice(), weekendPriceTypeEntity, currencyEntity, priceUnitsById);
        attachPriceDays(weekendEntity, priceGroup.getWeekendPrice().getDayOfWeekIds(), daysOfWeekById);
    }

    private void validateNotExceedingBase(String label, BigDecimal price, BigDecimal basePrice, Long currencyId) {
        if (price.compareTo(basePrice) > 0) {
            throw new IllegalArgumentException(
                    label + " price cannot exceed the BASE price (" + basePrice + ") for currency id: " + currencyId);
        }
    }

    private ResortRoomCategoryPriceEntity attachMainPriceRow(ResortRoomCategoryEntity entity,
                                                             ResortRoomCategoryPriceRequest priceRequest,
                                                             PriceTypeEntity priceTypeEntity,
                                                             CurrencyEntity currencyEntity,
                                                             Map<Long, PriceUnitEntity> priceUnitsById) {
        ResortRoomCategoryPriceEntity priceEntity = ResortRoomCategoryPriceMapper.create(
                priceRequest.getName(), priceRequest.getPrice(), priceTypeEntity,
                priceUnitsById.get(priceRequest.getPriceUnitId()), currencyEntity);
        entity.addResortRoomCategoryPriceEntity(priceEntity);
        return priceEntity;
    }

    private void attachPriceDays(ResortRoomCategoryPriceEntity priceEntity,
                                 List<Long> dayOfWeekIds,
                                 Map<Long, DayOfWeekEntity> daysOfWeekById) {
        for (Long dayOfWeekId : dayOfWeekIds) {
            DayOfWeekEntity dayOfWeekEntity = daysOfWeekById.get(dayOfWeekId);
            ResortRoomCategoryPriceDayEntity dayEntity = ResortRoomCategoryPriceDayMapper.create();
            priceEntity.addResortRoomCategoryPriceDayEntity(dayEntity);
            dayOfWeekEntity.addResortRoomCategoryPriceDayEntity(dayEntity);
        }
    }

    @Override
    public ResortRoomCategoryEntity getEntityById(Long resortId, Long id) {
        return resortRoomCategoryRepository.findByResortEntity_IdAndIdAndIsActiveAndIsDeleted(resortId, id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("ResortRoomCategory not found with id: " + id));
    }

    @Override
    public ResortRoomCategoryResponse getById(Long resortId, Long id) {
        ResortRoomCategoryEntity entity = getEntityById(resortId, id);
        return new ResortRoomCategoryResponse(buildDto(entity));
    }

    private ResortRoomCategoryDto buildDto(ResortRoomCategoryEntity entity) {
        ResortRoomCategoryMetaEntity resortRoomCategoryMetaEntity =
                resortRoomCategoryMetaService.getEntityByResortRoomCategoryId(entity.getId());
        return ResortRoomCategoryMapper.toDto(entity)
                .resort(ResortMapper.toDto(entity.getResortEntity()).build())
                .roomCategory(RoomCategoryMapper.toDto(entity.getRoomCategoryEntity()).build())
                .meta(ResortRoomCategoryMetaMapper.toDto(resortRoomCategoryMetaEntity).build())
                .beds(mapBeds(entity))
                .build();
    }

    @Override
    public ResortRoomCategoryCountResponse getActiveRoomCategoryCount(Long resortId) {
        List<String> codes = resortRoomCategoryRepository
                .findRoomCategoryCodeByResortEntity_IdAndIsActiveAndIsDeleted(resortId, true, false);
        return new ResortRoomCategoryCountResponse((long) codes.size(), codes);
    }

    private List<ResortRoomCategoryBedDto> mapBeds(ResortRoomCategoryEntity entity) {
        return entity.getResortRoomCategoryBedEntities().stream()
                .filter(bedEntity -> Boolean.TRUE.equals(bedEntity.getIsActive())
                        && Boolean.FALSE.equals(bedEntity.getIsDeleted()))
                .map(bedEntity -> ResortRoomCategoryBedMapper.toDto(bedEntity)
                        .bedType(BedTypeMapper.toDto(bedEntity.getBedTypeEntity()).build())
                        .build())
                .toList();
    }

    @Override
    public PaginatedResponse<ResortRoomCategoryDto> getAll(Long resortId, ResortRoomCategoryFilterRequest request) {
        Specification<@NonNull ResortRoomCategoryEntity> specification =
                ResortRoomCategorySpecification.filter(resortId, request, LocaleContext.getLocaleId());
        Pageable pageable = request.toPageable(ALLOWED_SORT_FIELDS, ResortRoomCategorySortField.localeSortFields());
        Page<@NonNull ResortRoomCategoryDto> page = resortRoomCategoryRepository
                .findAll(specification, pageable)
                .map(this::buildDto);
        return Pagination.buildPaginatedResponse(page, ALLOWED_SORT_FIELDS, ALLOWED_SEARCH_FIELDS);
    }

    @Transactional
    @Override
    public SuccessResponse update(ResortRoomCategoryEntity entity, UpdateResortRoomCategoryRequest request) {
        ResortRoomCategoryMapper.update(entity, request);
        resortRoomCategoryRepository.save(entity);
        log.info("ResortRoomCategory updated with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse delete(ResortRoomCategoryEntity entity) {
        entity.setIsDeleted(true);
        entity.setIsActive(false);

        entity.getResortRoomCategoryLocaleEntities().forEach(localeEntity -> {
            localeEntity.setIsDeleted(true);
            localeEntity.setIsActive(false);
        });

        entity.getResortRoomCategoryBedEntities().forEach(bedEntity -> {
            bedEntity.setIsDeleted(true);
            bedEntity.setIsActive(false);
        });

        entity.getResortRoomCategoryPriceEntities().forEach(priceEntity -> {
            priceEntity.setIsDeleted(true);
            priceEntity.setIsActive(false);

            priceEntity.getResortRoomCategoryPriceDayEntities().forEach(dayEntity -> {
                dayEntity.setIsDeleted(true);
                dayEntity.setIsActive(false);
            });
        });

        // ResortRoomCategoryMeta is soft-deleted in place, not via its own service — it's cascade=ALL
        // and already a managed entity in this transaction, so the field changes flush with entity below.
        ResortRoomCategoryMetaEntity resortRoomCategoryMetaEntity = resortRoomCategoryMetaService.getEntityByResortRoomCategoryId(entity.getId());
        resortRoomCategoryMetaEntity.setIsDeleted(true);
        resortRoomCategoryMetaEntity.setIsActive(false);

        resortRoomCategoryRepository.save(entity);
        log.info("ResortRoomCategory soft-deleted with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }
}
