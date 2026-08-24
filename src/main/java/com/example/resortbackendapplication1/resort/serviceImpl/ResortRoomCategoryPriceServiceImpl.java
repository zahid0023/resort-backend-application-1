package com.example.resortbackendapplication1.resort.serviceImpl;

import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.currency.model.entity.CurrencyEntity;
import com.example.resortbackendapplication1.currency.model.mapper.CurrencyMapper;
import com.example.resortbackendapplication1.dayofweek.model.entity.DayOfWeekEntity;
import com.example.resortbackendapplication1.dayofweek.model.mapper.DayOfWeekMapper;
import com.example.resortbackendapplication1.price.model.entity.PriceTypeEntity;
import com.example.resortbackendapplication1.price.model.entity.PriceUnitEntity;
import com.example.resortbackendapplication1.price.model.mapper.PriceTypeMapper;
import com.example.resortbackendapplication1.price.model.mapper.PriceUnitMapper;
import com.example.resortbackendapplication1.resort.dto.request.resortroomcategoryprice.CreateResortRoomCategoryHolidayPriceRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortroomcategoryprice.CreateResortRoomCategoryPriceGroupRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortroomcategoryprice.CreateResortRoomCategorySpecialPriceRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortroomcategoryprice.ResortRoomCategoryDateBoundPriceRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortroomcategoryprice.UpdateResortRoomCategoryPriceRequest;
import com.example.resortbackendapplication1.resort.dto.response.resortroomcategoryprices.CreateResortRoomCategoryPriceGroupResponse;
import com.example.resortbackendapplication1.resort.dto.response.resortroomcategoryprices.ResortRoomCategoryPriceGroupResponse;
import com.example.resortbackendapplication1.resort.dto.response.resortroomcategoryprices.ResortRoomCategoryPriceResponse;
import com.example.resortbackendapplication1.resort.model.dto.ResortRoomCategoryPriceDayDto;
import com.example.resortbackendapplication1.resort.model.dto.ResortRoomCategoryPriceDto;
import com.example.resortbackendapplication1.resort.model.dto.ResortRoomCategoryPriceGroupDto;
import com.example.resortbackendapplication1.resort.model.entity.ResortRoomCategoryEntity;
import com.example.resortbackendapplication1.resort.model.entity.ResortRoomCategoryPriceDayEntity;
import com.example.resortbackendapplication1.resort.model.entity.ResortRoomCategoryPriceEntity;
import com.example.resortbackendapplication1.resort.model.mapper.ResortRoomCategoryMapper;
import com.example.resortbackendapplication1.resort.model.mapper.ResortRoomCategoryPriceDayMapper;
import com.example.resortbackendapplication1.resort.model.mapper.ResortRoomCategoryPriceMapper;
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
     * {@link #update}. HOLIDAY/SPECIAL prices are unaffected and remain fully create/delete-able here.
     */
    private static final Set<String> LOCKED_PRICE_TYPE_CODES = Set.of("BAS", "WKD", "WKE");

    private final ResortRoomCategoryPriceRepository resortRoomCategoryPriceRepository;

    public ResortRoomCategoryPriceServiceImpl(ResortRoomCategoryPriceRepository resortRoomCategoryPriceRepository) {
        this.resortRoomCategoryPriceRepository = resortRoomCategoryPriceRepository;
    }

    @Transactional
    @Override
    public CreateResortRoomCategoryPriceGroupResponse createMain(CreateResortRoomCategoryPriceGroupRequest request,
                                                                   ResortRoomCategoryEntity resortRoomCategoryEntity,
                                                                   PriceTypeEntity basePriceTypeEntity,
                                                                   PriceTypeEntity weekdayPriceTypeEntity,
                                                                   PriceTypeEntity weekendPriceTypeEntity,
                                                                   CurrencyEntity currencyEntity,
                                                                   PriceUnitEntity basePriceUnitEntity,
                                                                   PriceUnitEntity weekdayPriceUnitEntity,
                                                                   PriceUnitEntity weekendPriceUnitEntity,
                                                                   List<DayOfWeekEntity> weekdayDayOfWeekEntities,
                                                                   List<DayOfWeekEntity> weekendDayOfWeekEntities) {
        if (resortRoomCategoryPriceRepository
                .findByResortRoomCategoryEntity_IdAndPriceTypeEntity_CodeAndCurrencyEntity_IdAndIsActiveAndIsDeleted(
                        resortRoomCategoryEntity.getId(), "BAS", currencyEntity.getId(), true, false)
                .isPresent()) {
            throw new IllegalStateException(
                    "This room category already has a price group for currency id: " + currencyEntity.getId());
        }
        if (request.getWeekdayPrice().compareTo(request.getBasePrice()) > 0) {
            throw new IllegalArgumentException(
                    "WKD price cannot exceed the BASE price (" + request.getBasePrice()
                            + ") for currency id: " + currencyEntity.getId());
        }
        if (request.getWeekendPrice().compareTo(request.getBasePrice()) > 0) {
            throw new IllegalArgumentException(
                    "WKE price cannot exceed the BASE price (" + request.getBasePrice()
                            + ") for currency id: " + currencyEntity.getId());
        }

        ResortRoomCategoryPriceEntity baseEntity = ResortRoomCategoryPriceMapper.create(
                "Base Price", request.getBasePrice(), basePriceTypeEntity, basePriceUnitEntity, currencyEntity);
        resortRoomCategoryEntity.addResortRoomCategoryPriceEntity(baseEntity);
        resortRoomCategoryPriceRepository.save(baseEntity);

        ResortRoomCategoryPriceEntity weekdayEntity = ResortRoomCategoryPriceMapper.create(
                "Weekday Price", request.getWeekdayPrice(), weekdayPriceTypeEntity, weekdayPriceUnitEntity, currencyEntity);
        resortRoomCategoryEntity.addResortRoomCategoryPriceEntity(weekdayEntity);
        attachDays(weekdayEntity, weekdayDayOfWeekEntities);
        resortRoomCategoryPriceRepository.save(weekdayEntity);

        ResortRoomCategoryPriceEntity weekendEntity = ResortRoomCategoryPriceMapper.create(
                "Weekend Price", request.getWeekendPrice(), weekendPriceTypeEntity, weekendPriceUnitEntity, currencyEntity);
        resortRoomCategoryEntity.addResortRoomCategoryPriceEntity(weekendEntity);
        attachDays(weekendEntity, weekendDayOfWeekEntities);
        resortRoomCategoryPriceRepository.save(weekendEntity);

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

    private SuccessResponse createDateBoundPrice(ResortRoomCategoryDateBoundPriceRequest request,
                                                  ResortRoomCategoryEntity resortRoomCategoryEntity,
                                                  PriceTypeEntity priceTypeEntity,
                                                  PriceUnitEntity priceUnitEntity,
                                                  CurrencyEntity currencyEntity) {
        if (resortRoomCategoryPriceRepository
                .existsByResortRoomCategoryEntity_IdAndPriceTypeEntity_IdAndPriceUnitEntity_IdAndCurrencyEntity_IdAndIsActiveAndIsDeleted(
                        resortRoomCategoryEntity.getId(), priceTypeEntity.getId(), priceUnitEntity.getId(), currencyEntity.getId(), true, false)) {
            throw new IllegalStateException("This room category already has an active price with this type/unit/currency combination");
        }

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
    public ResortRoomCategoryPriceResponse getById(Long resortRoomCategoryId, Long id) {
        ResortRoomCategoryPriceEntity entity = getEntityById(resortRoomCategoryId, id);
        ResortRoomCategoryPriceDto dto = buildDto(entity).days(mapDays(entity)).build();
        return new ResortRoomCategoryPriceResponse(dto);
    }

    @Override
    public ResortRoomCategoryPriceGroupResponse getAllGroupedByCurrency(Long resortRoomCategoryId, CurrencyEntity currencyEntity) {
        List<ResortRoomCategoryPriceEntity> entities = resortRoomCategoryPriceRepository
                .findByResortRoomCategoryEntity_IdAndCurrencyEntity_IdAndIsActiveAndIsDeleted(
                        resortRoomCategoryId, currencyEntity.getId(), true, false);

        ResortRoomCategoryPriceDto base = null;
        ResortRoomCategoryPriceDto weekday = null;
        ResortRoomCategoryPriceDto weekend = null;
        List<ResortRoomCategoryPriceDto> holidays = new ArrayList<>();
        List<ResortRoomCategoryPriceDto> specials = new ArrayList<>();

        for (ResortRoomCategoryPriceEntity entity : entities) {
            ResortRoomCategoryPriceDto dto = buildDto(entity).days(mapDays(entity)).build();
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

        ResortRoomCategoryPriceGroupDto groupDto = ResortRoomCategoryPriceGroupDto.builder()
                .currency(CurrencyMapper.toDto(currencyEntity).build())
                .base(base)
                .weekday(weekday)
                .weekend(weekend)
                .holidays(holidays)
                .specials(specials)
                .build();
        return new ResortRoomCategoryPriceGroupResponse(groupDto);
    }

    @Transactional
    @Override
    public SuccessResponse update(ResortRoomCategoryPriceEntity entity,
                                  UpdateResortRoomCategoryPriceRequest request,
                                  List<DayOfWeekEntity> dayOfWeekEntities) {
        ResortRoomCategoryPriceMapper.update(entity, request);

        for (ResortRoomCategoryPriceDayEntity dayEntity : new ArrayList<>(entity.getResortRoomCategoryPriceDayEntities())) {
            DayOfWeekEntity dayOfWeekEntity = dayEntity.getDayOfWeekEntity();
            entity.removeResortRoomCategoryPriceDayEntity(dayEntity);
            dayOfWeekEntity.removeResortRoomCategoryPriceDayEntity(dayEntity);
        }
        attachDays(entity, dayOfWeekEntities);

        resortRoomCategoryPriceRepository.save(entity);
        log.info("ResortRoomCategoryPrice updated with id: {}", entity.getId());
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

    private void attachDays(ResortRoomCategoryPriceEntity entity, List<DayOfWeekEntity> dayOfWeekEntities) {
        for (DayOfWeekEntity dayOfWeekEntity : dayOfWeekEntities) {
            ResortRoomCategoryPriceDayEntity dayEntity = ResortRoomCategoryPriceDayMapper.create();
            entity.addResortRoomCategoryPriceDayEntity(dayEntity);
            dayOfWeekEntity.addResortRoomCategoryPriceDayEntity(dayEntity);
        }
    }

    private ResortRoomCategoryPriceDto.ResortRoomCategoryPriceDtoBuilder buildDto(ResortRoomCategoryPriceEntity entity) {
        return ResortRoomCategoryPriceMapper.toDto(entity)
                .resortRoomCategory(ResortRoomCategoryMapper.toDto(entity.getResortRoomCategoryEntity()).build())
                .priceType(PriceTypeMapper.toDto(entity.getPriceTypeEntity()).build())
                .priceUnit(PriceUnitMapper.toDto(entity.getPriceUnitEntity()).build())
                .currency(CurrencyMapper.toDto(entity.getCurrencyEntity()).build());
    }

    private List<ResortRoomCategoryPriceDayDto> mapDays(ResortRoomCategoryPriceEntity entity) {
        return entity.getResortRoomCategoryPriceDayEntities().stream()
                .filter(dayEntity -> Boolean.TRUE.equals(dayEntity.getIsActive())
                        && Boolean.FALSE.equals(dayEntity.getIsDeleted()))
                .map(dayEntity -> ResortRoomCategoryPriceDayMapper.toDto(dayEntity)
                        .dayOfWeek(DayOfWeekMapper.toDto(dayEntity.getDayOfWeekEntity()).build())
                        .build())
                .toList();
    }
}
