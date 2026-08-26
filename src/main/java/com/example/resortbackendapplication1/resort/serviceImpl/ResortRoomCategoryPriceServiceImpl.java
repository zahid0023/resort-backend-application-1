package com.example.resortbackendapplication1.resort.serviceImpl;

import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.currency.model.entity.CurrencyEntity;
import com.example.resortbackendapplication1.currency.model.mapper.CurrencyMapper;
import com.example.resortbackendapplication1.dayofweek.model.mapper.DayOfWeekMapper;
import com.example.resortbackendapplication1.price.model.entity.PriceTypeEntity;
import com.example.resortbackendapplication1.price.model.entity.PriceUnitEntity;
import com.example.resortbackendapplication1.price.model.mapper.PriceTypeMapper;
import com.example.resortbackendapplication1.price.model.mapper.PriceUnitMapper;
import com.example.resortbackendapplication1.resort.dto.request.resortroomcategoryprice.CreateResortRoomCategoryHolidayPriceRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortroomcategoryprice.CreateResortRoomCategoryMainPriceRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortroomcategoryprice.CreateResortRoomCategorySpecialPriceRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortroomcategoryprice.ResortRoomCategoryDateRangePriceRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortroomcategoryprice.UpdateResortRoomCategoryHolidayPriceRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortroomcategoryprice.UpdateResortRoomCategoryMainPriceRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortroomcategoryprice.UpdateResortRoomCategorySpecialPriceRequest;
import com.example.resortbackendapplication1.resort.dto.response.resortroomcategoryprices.CreateResortRoomCategoryPriceGroupResponse;
import com.example.resortbackendapplication1.resort.dto.response.resortroomcategoryprices.ResortRoomCategoryPriceCountResponse;
import com.example.resortbackendapplication1.resort.dto.response.resortroomcategoryprices.ResortRoomCategoryPriceGroupResponse;
import com.example.resortbackendapplication1.resort.model.dto.ResortRoomCategoryMainPriceDto;
import com.example.resortbackendapplication1.resort.model.dto.ResortRoomCategoryPriceDto;
import com.example.resortbackendapplication1.resort.model.dto.ResortRoomCategoryPriceGroupDto;
import com.example.resortbackendapplication1.resort.model.dto.ResortWeeklyScheduleDayDto;
import com.example.resortbackendapplication1.resort.model.entity.ResortRoomCategoryEntity;
import com.example.resortbackendapplication1.resort.model.entity.ResortRoomCategoryPriceEntity;
import com.example.resortbackendapplication1.resort.model.entity.ResortWeeklyScheduleDayEntity;
import com.example.resortbackendapplication1.resort.model.mapper.ResortRoomCategoryMapper;
import com.example.resortbackendapplication1.resort.model.mapper.ResortRoomCategoryPriceMapper;
import com.example.resortbackendapplication1.resort.model.mapper.ResortWeeklyScheduleDayMapper;
import com.example.resortbackendapplication1.resort.repository.ResortRoomCategoryPriceRepository;
import com.example.resortbackendapplication1.resort.service.ResortRoomCategoryPriceService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class ResortRoomCategoryPriceServiceImpl implements ResortRoomCategoryPriceService {

    /**
     * BASE/WEEKDAY/WEEKEND prices are only ever created together with the resort room category itself
     * (see {@code ResortRoomCategoryServiceImpl.create}) and can never be deleted — only updated, via
     * {@link #updateMain}. HOLIDAY/SPECIAL prices are unaffected and remain fully create/delete-able here.
     */
    private static final Set<String> LOCKED_PRICE_TYPE_CODES = Set.of("BAS", "WKD", "WKE");

    private final ResortRoomCategoryPriceRepository resortRoomCategoryPriceRepository;

    public ResortRoomCategoryPriceServiceImpl(ResortRoomCategoryPriceRepository resortRoomCategoryPriceRepository) {
        this.resortRoomCategoryPriceRepository = resortRoomCategoryPriceRepository;
    }

    @Transactional
    @Override
    public CreateResortRoomCategoryPriceGroupResponse createMain(CreateResortRoomCategoryMainPriceRequest request,
                                                                   ResortRoomCategoryEntity resortRoomCategoryEntity,
                                                                   PriceTypeEntity basePriceTypeEntity,
                                                                   PriceTypeEntity weekdayPriceTypeEntity,
                                                                   PriceTypeEntity weekendPriceTypeEntity,
                                                                   CurrencyEntity currencyEntity,
                                                                   PriceUnitEntity basePriceUnitEntity,
                                                                   PriceUnitEntity weekdayPriceUnitEntity,
                                                                   PriceUnitEntity weekendPriceUnitEntity) {
        if (resortRoomCategoryPriceRepository
                .findByResortRoomCategoryEntity_IdAndPriceTypeEntity_CodeAndCurrencyEntity_IdAndIsActiveAndIsDeleted(
                        resortRoomCategoryEntity.getId(), "BAS", currencyEntity.getId(), true, false)
                .isPresent()) {
            throw new IllegalStateException(
                    "This room category already has a price group for currency id: " + currencyEntity.getId());
        }
        List<ResortRoomCategoryPriceEntity> entities = ResortRoomCategoryPriceMapper.buildMainSet(
                request, resortRoomCategoryEntity, basePriceTypeEntity, weekdayPriceTypeEntity, weekendPriceTypeEntity,
                currencyEntity, basePriceUnitEntity, weekdayPriceUnitEntity, weekendPriceUnitEntity);
        resortRoomCategoryPriceRepository.saveAll(entities);

        ResortRoomCategoryPriceEntity baseEntity = entities.get(0);
        ResortRoomCategoryPriceEntity weekdayEntity = entities.get(1);
        ResortRoomCategoryPriceEntity weekendEntity = entities.get(2);

        log.info("ResortRoomCategoryPrice group created for resort room category id: {}, currency id: {} "
                        + "(base={}, weekday={}, weekend={})",
                resortRoomCategoryEntity.getId(), currencyEntity.getId(),
                baseEntity.getId(), weekdayEntity.getId(), weekendEntity.getId());
        return new CreateResortRoomCategoryPriceGroupResponse(
                true, baseEntity.getId(), weekdayEntity.getId(), weekendEntity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse createHoliday(CreateResortRoomCategoryHolidayPriceRequest request,
                                         ResortRoomCategoryEntity resortRoomCategoryEntity,
                                         PriceTypeEntity holidayPriceTypeEntity,
                                         PriceUnitEntity priceUnitEntity,
                                         CurrencyEntity currencyEntity) {
        return createDateBoundPrice(request, resortRoomCategoryEntity, holidayPriceTypeEntity, priceUnitEntity, currencyEntity);
    }

    @Transactional
    @Override
    public SuccessResponse createSpecial(CreateResortRoomCategorySpecialPriceRequest request,
                                         ResortRoomCategoryEntity resortRoomCategoryEntity,
                                         PriceTypeEntity specialPriceTypeEntity,
                                         PriceUnitEntity priceUnitEntity,
                                         CurrencyEntity currencyEntity) {
        return createDateBoundPrice(request, resortRoomCategoryEntity, specialPriceTypeEntity, priceUnitEntity, currencyEntity);
    }

    private SuccessResponse createDateBoundPrice(ResortRoomCategoryDateRangePriceRequest request,
                                                  ResortRoomCategoryEntity resortRoomCategoryEntity,
                                                  PriceTypeEntity priceTypeEntity,
                                                  PriceUnitEntity priceUnitEntity,
                                                  CurrencyEntity currencyEntity) {
        // Locked (not the plain findByResortRoomCategoryEntity_Id...CurrencyEntity_Id... lookup) so this
        // contends on the same physical row deleteByCurrency locks via findActiveBasePricesForUpdate — closes
        // the write-skew race where this check and a concurrent deleteByCurrency call for the same currency
        // could each read a stale, independent snapshot and both proceed, orphaning a freshly created row.
        if (resortRoomCategoryPriceRepository
                .findMainBasePriceForUpdate(resortRoomCategoryEntity.getId(), currencyEntity.getId())
                .isEmpty()) {
            throw new EntityNotFoundException(
                    "This room category has no active main price for currency id: " + currencyEntity.getId()
                            + " — create a main price for this currency first");
        }
        // HOLIDAY/SPECIAL prices are date-ranged rules, not a single slot per type/unit/currency
        // like BASE/WEEKDAY/WEEKEND — a room category/currency can have many active HOLIDAY/SPECIAL
        // rows at once (Christmas, Eid, a summer sale, ...), so no duplicate-active check here.
        ResortRoomCategoryPriceEntity entity = ResortRoomCategoryPriceMapper.create(request, priceTypeEntity, priceUnitEntity, currencyEntity);
        resortRoomCategoryEntity.addResortRoomCategoryPriceEntity(entity);

        resortRoomCategoryPriceRepository.save(entity);
        log.info("ResortRoomCategoryPrice ({}) created with id: {}", priceTypeEntity.getCode(), entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public ResortRoomCategoryPriceEntity getEntityById(Long resortRoomCategoryId, Long id) {
        return resortRoomCategoryPriceRepository
                .findByResortRoomCategoryEntity_IdAndIdAndIsActiveAndIsDeleted(resortRoomCategoryId, id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("ResortRoomCategoryPrice not found with id: " + id));
    }

    @Override
    public ResortRoomCategoryPriceGroupResponse getAllGroupedByCurrency(Long resortRoomCategoryId, CurrencyEntity currencyEntity,
                                                                        List<ResortWeeklyScheduleDayEntity> weekdayScheduleDays,
                                                                        List<ResortWeeklyScheduleDayEntity> weekendScheduleDays) {
        List<ResortRoomCategoryPriceEntity> entities = resortRoomCategoryPriceRepository
                .findByResortRoomCategoryEntity_IdAndCurrencyEntity_IdAndIsActiveAndIsDeleted(
                        resortRoomCategoryId, currencyEntity.getId(), true, false);

        ResortRoomCategoryPriceDto base = null;
        ResortRoomCategoryPriceDto weekday = null;
        ResortRoomCategoryPriceDto weekend = null;
        List<ResortRoomCategoryPriceDto> holidays = new ArrayList<>();
        List<ResortRoomCategoryPriceDto> specials = new ArrayList<>();

        for (ResortRoomCategoryPriceEntity entity : entities) {
            ResortRoomCategoryPriceDto dto = buildDto(entity)
                    .days(mapDays(entity, weekdayScheduleDays, weekendScheduleDays))
                    .build();
            switch (entity.getPriceTypeEntity().getCode()) {
                case "BAS" -> base = dto;
                case "WKD" -> weekday = dto;
                case "WKE" -> weekend = dto;
                case "HOL" -> holidays.add(dto);
                case "SPECIAL" -> specials.add(dto);
                default -> log.warn("Unhandled price type code {} for resort room category price {}",
                        entity.getPriceTypeEntity().getCode(), entity.getId());
            }
        }

        ResortRoomCategoryMainPriceDto mainDto = ResortRoomCategoryMainPriceDto.builder()
                .base(base)
                .weekday(weekday)
                .weekend(weekend)
                .build();

        ResortRoomCategoryPriceGroupDto groupDto = ResortRoomCategoryPriceGroupDto.builder()
                .currency(CurrencyMapper.toDto(currencyEntity).build())
                .main(mainDto)
                .holidays(holidays)
                .specials(specials)
                .build();
        return new ResortRoomCategoryPriceGroupResponse(groupDto);
    }

    @Override
    public ResortRoomCategoryPriceCountResponse getCount(Long resortRoomCategoryId) {
        List<String> codes = resortRoomCategoryPriceRepository
                .findDistinctCurrencyCodeByResortRoomCategoryEntity_IdAndPriceTypeEntity_CodeAndIsActiveAndIsDeleted(
                        resortRoomCategoryId, "BAS", true, false);
        return new ResortRoomCategoryPriceCountResponse((long) codes.size(), codes);
    }

    @Transactional
    @Override
    public SuccessResponse updateMain(ResortRoomCategoryEntity resortRoomCategoryEntity,
                                      CurrencyEntity currencyEntity,
                                      UpdateResortRoomCategoryMainPriceRequest request,
                                      PriceTypeEntity basePriceTypeEntity,
                                      PriceTypeEntity weekdayPriceTypeEntity,
                                      PriceTypeEntity weekendPriceTypeEntity,
                                      PriceUnitEntity basePriceUnitEntity,
                                      PriceUnitEntity weekdayPriceUnitEntity,
                                      PriceUnitEntity weekendPriceUnitEntity) {
        List<ResortRoomCategoryPriceEntity> existing = List.of(
                findMainRow(resortRoomCategoryEntity, currencyEntity, "BAS"),
                findMainRow(resortRoomCategoryEntity, currencyEntity, "WKD"),
                findMainRow(resortRoomCategoryEntity, currencyEntity, "WKE"));

        existing.forEach(row -> {
            row.setIsDeleted(true);
            row.setIsActive(false);
        });
        // Flushed before the new rows are built/inserted: the partial unique index
        // uq_resort_room_category_price only excludes is_active=false/is_deleted=true rows,
        // and Hibernate would otherwise send the INSERTs before these UPDATEs in the same
        // flush (inserts always precede updates in Hibernate's action queue ordering),
        // tripping the constraint against the still-active old rows.
        resortRoomCategoryPriceRepository.saveAllAndFlush(existing);

        List<ResortRoomCategoryPriceEntity> created = ResortRoomCategoryPriceMapper.buildMainSet(
                request, resortRoomCategoryEntity, basePriceTypeEntity, weekdayPriceTypeEntity, weekendPriceTypeEntity,
                currencyEntity, basePriceUnitEntity, weekdayPriceUnitEntity, weekendPriceUnitEntity);
        resortRoomCategoryPriceRepository.saveAll(created);
        log.info("ResortRoomCategoryPrice main price set for resort room category id: {}, currency id: {} "
                        + "({} rows, replacing {} old rows)",
                resortRoomCategoryEntity.getId(), currencyEntity.getId(), created.size(), existing.size());
        return new SuccessResponse(true, created.get(0).getId());
    }

    private ResortRoomCategoryPriceEntity findMainRow(ResortRoomCategoryEntity resortRoomCategoryEntity,
                                                       CurrencyEntity currencyEntity,
                                                       String priceTypeCode) {
        return resortRoomCategoryPriceRepository
                .findByResortRoomCategoryEntity_IdAndPriceTypeEntity_CodeAndCurrencyEntity_IdAndIsActiveAndIsDeleted(
                        resortRoomCategoryEntity.getId(), priceTypeCode, currencyEntity.getId(), true, false)
                .orElseThrow(() -> new EntityNotFoundException(
                        priceTypeCode + " price not found for currency id: " + currencyEntity.getId()));
    }

    @Transactional
    @Override
    public SuccessResponse updateHoliday(ResortRoomCategoryPriceEntity entity,
                                         UpdateResortRoomCategoryHolidayPriceRequest request,
                                         PriceUnitEntity priceUnitEntity) {
        validatePriceTypeCode(entity, "HOL");
        return updateDateRangePrice(entity, request, priceUnitEntity);
    }

    @Transactional
    @Override
    public SuccessResponse updateSpecial(ResortRoomCategoryPriceEntity entity,
                                         UpdateResortRoomCategorySpecialPriceRequest request,
                                         PriceUnitEntity priceUnitEntity) {
        validatePriceTypeCode(entity, "SPECIAL");
        return updateDateRangePrice(entity, request, priceUnitEntity);
    }

    /**
     * Blocks e.g. {@code PUT .../specials/{id}} from being called with an id that actually belongs to a
     * HOLIDAY row (or any other type) — {@code ResortRoomCategoryPriceController.resolvePriceEntity} only
     * scopes by room category, not price type, so without this a client hitting the wrong endpoint would
     * silently update the row while its stored {@code price_type_id} stayed unchanged.
     */
    private void validatePriceTypeCode(ResortRoomCategoryPriceEntity entity, String expectedPriceTypeCode) {
        String actualPriceTypeCode = entity.getPriceTypeEntity().getCode();
        if (!expectedPriceTypeCode.equals(actualPriceTypeCode)) {
            throw new IllegalArgumentException(
                    "Price id: " + entity.getId() + " is a " + actualPriceTypeCode
                            + " price, not a " + expectedPriceTypeCode + " price");
        }
    }

    private SuccessResponse updateDateRangePrice(ResortRoomCategoryPriceEntity entity,
                                                 ResortRoomCategoryDateRangePriceRequest request,
                                                 PriceUnitEntity priceUnitEntity) {
        ResortRoomCategoryPriceMapper.updateDateRange(entity, request, priceUnitEntity);
        resortRoomCategoryPriceRepository.save(entity);
        log.info("ResortRoomCategoryPrice ({}) updated with id: {}", entity.getPriceTypeEntity().getCode(), entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse delete(ResortRoomCategoryPriceEntity entity) {
        String priceTypeCode = entity.getPriceTypeEntity().getCode();
        if (LOCKED_PRICE_TYPE_CODES.contains(priceTypeCode)) {
            throw new IllegalArgumentException(priceTypeCode + " prices cannot be deleted through this endpoint");
        }

        entity.setIsDeleted(true);
        entity.setIsActive(false);
        resortRoomCategoryPriceRepository.save(entity);
        log.info("ResortRoomCategoryPrice soft-deleted with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse deleteByCurrency(ResortRoomCategoryEntity resortRoomCategoryEntity, CurrencyEntity currencyEntity) {
        // Locked, and deliberately acquired BEFORE fetching this currency's own row set below — a concurrent
        // createHoliday/createSpecial call for this same currency locks the same physical BAS row (see
        // findMainBasePriceForUpdate), so this either blocks until that call commits (and then the fetch below
        // picks up its freshly created row too) or forces that call to block until this delete commits (and
        // then it correctly finds no active BAS left). BAS is created/replaced only as part of the same
        // main-set bundle as WKD/WKE (see buildMainSet), so counting distinct currencies with an active BAS
        // row is the same reliable "how many currencies does this room category have" signal getCount() uses.
        List<ResortRoomCategoryPriceEntity> activeBaseRows = resortRoomCategoryPriceRepository
                .findActiveBasePricesForUpdate(resortRoomCategoryEntity.getId());

        // Fetched after the lock above is acquired (not before), so a currency whose main price was created
        // by a concurrent call that just committed while this call was blocked is correctly seen here — and
        // the 404 check below runs before the last-currency 409 check, so deleting a currency that was never
        // priced at all (in a room category that otherwise has exactly one other priced currency) still 404s
        // rather than being misreported as "cannot delete the last currency."
        List<ResortRoomCategoryPriceEntity> entities = resortRoomCategoryPriceRepository
                .findByResortRoomCategoryEntity_IdAndCurrencyEntity_IdAndIsActiveAndIsDeleted(
                        resortRoomCategoryEntity.getId(), currencyEntity.getId(), true, false);
        if (entities.isEmpty()) {
            throw new EntityNotFoundException(
                    "No active prices found for currency id: " + currencyEntity.getId());
        }

        long currencyCountWithMain = activeBaseRows.stream()
                .map(row -> row.getCurrencyEntity().getId())
                .distinct()
                .count();
        if (currencyCountWithMain <= 1) {
            throw new IllegalStateException(
                    "At least one currency's prices must remain for this room category — cannot delete the last currency.");
        }

        entities.forEach(entity -> {
            entity.setIsDeleted(true);
            entity.setIsActive(false);
        });
        resortRoomCategoryPriceRepository.saveAll(entities);
        log.info("ResortRoomCategoryPrice soft-deleted all {} rows for resort room category id: {}, currency id: {}",
                entities.size(), resortRoomCategoryEntity.getId(), currencyEntity.getId());
        return new SuccessResponse(true, currencyEntity.getId());
    }

    private ResortRoomCategoryPriceDto.ResortRoomCategoryPriceDtoBuilder buildDto(ResortRoomCategoryPriceEntity entity) {
        return ResortRoomCategoryPriceMapper.toDto(entity)
                .resortRoomCategory(ResortRoomCategoryMapper.toDto(entity.getResortRoomCategoryEntity()).build())
                .priceType(PriceTypeMapper.toDto(entity.getPriceTypeEntity()).build())
                .priceUnit(PriceUnitMapper.toDto(entity.getPriceUnitEntity()).build())
                .currency(CurrencyMapper.toDto(entity.getCurrencyEntity()).build());
    }

    private List<ResortWeeklyScheduleDayDto> mapDays(ResortRoomCategoryPriceEntity entity,
                                                       List<ResortWeeklyScheduleDayEntity> weekdayScheduleDays,
                                                       List<ResortWeeklyScheduleDayEntity> weekendScheduleDays) {
        List<ResortWeeklyScheduleDayEntity> scheduleDays = switch (entity.getPriceTypeEntity().getCode()) {
            case "WKD" -> weekdayScheduleDays;
            case "WKE" -> weekendScheduleDays;
            default -> List.of();
        };
        return scheduleDays.stream()
                .map(dayEntity -> ResortWeeklyScheduleDayMapper.toDto(dayEntity)
                        .dayOfWeek(DayOfWeekMapper.toDto(dayEntity.getDayOfWeekEntity()).build())
                        .build())
                .toList();
    }
}
