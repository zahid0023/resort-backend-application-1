package com.example.resortbackendapplication1.resort.room.serviceImpl;

import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.currency.model.entity.CurrencyEntity;
import com.example.resortbackendapplication1.currency.model.mapper.CurrencyMapper;
import com.example.resortbackendapplication1.dayofweek.model.mapper.DayOfWeekMapper;
import com.example.resortbackendapplication1.price.model.entity.PriceUnitEntity;
import com.example.resortbackendapplication1.price.model.mapper.PriceUnitMapper;
import com.example.resortbackendapplication1.resort.room.dto.request.resortroomprice.CreateResortRoomMainPriceRequest;
import com.example.resortbackendapplication1.resort.room.dto.request.resortroomprice.CreateResortRoomSpecialPriceRequest;
import com.example.resortbackendapplication1.resort.room.dto.request.resortroomprice.UpdateResortRoomMainPriceRequest;
import com.example.resortbackendapplication1.resort.room.dto.request.resortroomprice.UpdateResortRoomSpecialPriceRequest;
import com.example.resortbackendapplication1.resort.room.dto.response.resortroomprices.ResortRoomPriceCountResponse;
import com.example.resortbackendapplication1.resort.room.dto.response.resortroomprices.ResortRoomPriceGroupResponse;
import com.example.resortbackendapplication1.resort.roomcategory.model.dto.ResortRoomCategoryPriceGroupDto;
import com.example.resortbackendapplication1.resort.room.model.dto.ResortRoomDateRangePriceDto;
import com.example.resortbackendapplication1.resort.room.model.dto.ResortRoomMainPriceDto;
import com.example.resortbackendapplication1.resort.room.model.dto.ResortRoomPriceGroupDto;
import com.example.resortbackendapplication1.resort.core.model.dto.ResortWeeklyScheduleDayDto;
import com.example.resortbackendapplication1.resort.room.model.entity.ResortRoomEntity;
import com.example.resortbackendapplication1.resort.room.model.entity.ResortRoomMainPriceEntity;
import com.example.resortbackendapplication1.resort.room.model.entity.ResortRoomSpecialPriceEntity;
import com.example.resortbackendapplication1.resort.core.model.entity.ResortWeeklyScheduleDayEntity;
import com.example.resortbackendapplication1.resort.room.model.mapper.ResortRoomMapper;
import com.example.resortbackendapplication1.resort.room.model.mapper.ResortRoomPriceMapper;
import com.example.resortbackendapplication1.resort.core.model.mapper.ResortWeeklyScheduleDayMapper;
import com.example.resortbackendapplication1.resort.room.repository.ResortRoomMainPriceRepository;
import com.example.resortbackendapplication1.resort.room.repository.ResortRoomSpecialPriceRepository;
import com.example.resortbackendapplication1.resort.room.service.ResortRoomPriceService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ResortRoomPriceServiceImpl implements ResortRoomPriceService {

    private final ResortRoomMainPriceRepository resortRoomMainPriceRepository;
    private final ResortRoomSpecialPriceRepository resortRoomSpecialPriceRepository;

    public ResortRoomPriceServiceImpl(ResortRoomMainPriceRepository resortRoomMainPriceRepository,
                                      ResortRoomSpecialPriceRepository resortRoomSpecialPriceRepository) {
        this.resortRoomMainPriceRepository = resortRoomMainPriceRepository;
        this.resortRoomSpecialPriceRepository = resortRoomSpecialPriceRepository;
    }

    @Transactional
    @Override
    public SuccessResponse createMain(CreateResortRoomMainPriceRequest request,
                                      ResortRoomEntity resortRoomEntity,
                                      CurrencyEntity currencyEntity,
                                      PriceUnitEntity priceUnitEntity) {
        if (resortRoomMainPriceRepository
                .findByResortRoomEntity_IdAndCurrencyEntity_IdAndIsActiveAndIsDeleted(
                        resortRoomEntity.getId(), currencyEntity.getId(), true, false)
                .isPresent()) {
            throw new IllegalStateException(
                    "This room already has an active main price override for currency id: " + currencyEntity.getId());
        }
        ResortRoomMainPriceEntity entity = ResortRoomPriceMapper.createMain(
                request, resortRoomEntity, priceUnitEntity, currencyEntity);
        resortRoomMainPriceRepository.save(entity);
        log.info("ResortRoomMainPrice created for resort room id: {}, currency id: {}, id: {}",
                resortRoomEntity.getId(), currencyEntity.getId(), entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse createSpecial(CreateResortRoomSpecialPriceRequest request,
                                         ResortRoomEntity resortRoomEntity,
                                         CurrencyEntity currencyEntity,
                                         PriceUnitEntity priceUnitEntity,
                                         boolean categoryHasActiveMain) {
        requireResolvableMainPrice(resortRoomEntity, currencyEntity, categoryHasActiveMain);
        ResortRoomSpecialPriceEntity entity = ResortRoomPriceMapper.createSpecial(
                request, resortRoomEntity, priceUnitEntity, currencyEntity);
        resortRoomSpecialPriceRepository.save(entity);
        log.info("ResortRoomSpecialPrice created with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    /**
     * A Special override needs *some* main price resolvable for this currency — the room's own, or (now that
     * Main/Specials are independent) its category's — not necessarily the room's own. The room's own row is
     * still checked via the locked {@code findForUpdate}, so this still contends on the same physical row
     * {@code deleteByCurrency} locks when the room does have its own override — closing the write-skew race
     * where this check and a concurrent {@code deleteByCurrency} call for the same currency could each read a
     * stale, independent snapshot and both proceed, orphaning a freshly created row. When the room has no own
     * override, {@code categoryHasActiveMain} (an unlocked existence check, per
     * {@code ResortRoomCategoryMainPriceRepository.existsBy...}) decides instead — there is no row of the
     * room's own to lock in that case. Mirrors {@code ResortRoomCategoryPriceServiceImpl.requireActiveMainPrice}.
     */
    private void requireResolvableMainPrice(ResortRoomEntity resortRoomEntity, CurrencyEntity currencyEntity,
                                             boolean categoryHasActiveMain) {
        boolean ownMainActive = resortRoomMainPriceRepository
                .findForUpdate(resortRoomEntity.getId(), currencyEntity.getId())
                .isPresent();
        if (!ownMainActive && !categoryHasActiveMain) {
            throw new EntityNotFoundException(
                    "No active main price resolvable for currency id: " + currencyEntity.getId()
                            + " — this room has none of its own, and its category has none either;"
                            + " create a main price for this currency first");
        }
    }

    @Override
    public ResortRoomSpecialPriceEntity getSpecialEntityById(Long resortRoomId, Long id) {
        return resortRoomSpecialPriceRepository
                .findByResortRoomEntity_IdAndIdAndIsActiveAndIsDeleted(resortRoomId, id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("ResortRoomSpecialPrice not found with id: " + id));
    }

    @Override
    public Optional<ResortRoomMainPriceEntity> getMainEntityByCurrency(Long resortRoomId, Long currencyId) {
        return resortRoomMainPriceRepository
                .findByResortRoomEntity_IdAndCurrencyEntity_IdAndIsActiveAndIsDeleted(resortRoomId, currencyId, true, false);
    }

    @Override
    public List<ResortRoomSpecialPriceEntity> getSpecialEntitiesByCurrency(Long resortRoomId, Long currencyId) {
        return resortRoomSpecialPriceRepository
                .findByResortRoomEntity_IdAndCurrencyEntity_IdAndIsActiveAndIsDeleted(resortRoomId, currencyId, true, false);
    }

    @Override
    public ResortRoomPriceGroupResponse getAllGroupedByCurrency(Long resortRoomId, CurrencyEntity currencyEntity,
                                                                 List<ResortWeeklyScheduleDayEntity> weekdayScheduleDays,
                                                                 List<ResortWeeklyScheduleDayEntity> weekendScheduleDays,
                                                                 ResortRoomCategoryPriceGroupDto categoryFallback) {
        var ownMain = resortRoomMainPriceRepository
                .findByResortRoomEntity_IdAndCurrencyEntity_IdAndIsActiveAndIsDeleted(
                        resortRoomId, currencyEntity.getId(), true, false);
        var ownSpecials = resortRoomSpecialPriceRepository
                .findByResortRoomEntity_IdAndCurrencyEntity_IdAndIsActiveAndIsDeleted(
                        resortRoomId, currencyEntity.getId(), true, false);

        List<ResortWeeklyScheduleDayDto> weekdayDays = mapDays(weekdayScheduleDays);
        List<ResortWeeklyScheduleDayDto> weekendDays = mapDays(weekendScheduleDays);

        boolean mainInherited = ownMain.isEmpty();
        boolean specialsInherited = ownSpecials.isEmpty();

        ResortRoomMainPriceDto mainDto = mainInherited
                ? ResortRoomPriceMapper.fromCategoryMain(categoryFallback.getMain())
                : buildMainDto(ownMain.get(), weekdayDays, weekendDays);

        List<ResortRoomDateRangePriceDto> specials = specialsInherited
                ? categoryFallback.getSpecials().stream()
                        .map(ResortRoomPriceMapper::fromCategoryDateRange)
                        .toList()
                : ownSpecials.stream()
                        .map(entity -> buildSpecialDto(entity, weekdayDays, weekendDays))
                        .toList();

        ResortRoomPriceGroupDto groupDto = ResortRoomPriceGroupDto.builder()
                .currency(CurrencyMapper.toDto(currencyEntity).build())
                .mainInherited(mainInherited)
                .specialsInherited(specialsInherited)
                .main(mainDto)
                .specials(specials)
                .build();
        return new ResortRoomPriceGroupResponse(groupDto);
    }

    @Override
    public ResortRoomPriceCountResponse getCount(Long resortRoomId) {
        List<String> codes = resortRoomMainPriceRepository
                .findDistinctCurrencyCodeByResortRoomEntity_IdAndIsActiveAndIsDeleted(
                        resortRoomId, true, false);
        return new ResortRoomPriceCountResponse((long) codes.size(), codes);
    }

    @Transactional
    @Override
    public SuccessResponse updateMain(ResortRoomEntity resortRoomEntity,
                                      CurrencyEntity currencyEntity,
                                      UpdateResortRoomMainPriceRequest request,
                                      PriceUnitEntity priceUnitEntity) {
        ResortRoomMainPriceEntity entity = resortRoomMainPriceRepository
                .findByResortRoomEntity_IdAndCurrencyEntity_IdAndIsActiveAndIsDeleted(
                        resortRoomEntity.getId(), currencyEntity.getId(), true, false)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Main price override not found for currency id: " + currencyEntity.getId()));
        ResortRoomPriceMapper.updateMain(entity, request, priceUnitEntity);
        resortRoomMainPriceRepository.save(entity);
        log.info("ResortRoomMainPrice updated for resort room id: {}, currency id: {}, id: {}",
                resortRoomEntity.getId(), currencyEntity.getId(), entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse updateSpecial(ResortRoomSpecialPriceEntity entity,
                                         UpdateResortRoomSpecialPriceRequest request,
                                         PriceUnitEntity priceUnitEntity) {
        ResortRoomPriceMapper.updateSpecial(entity, request, priceUnitEntity);
        resortRoomSpecialPriceRepository.save(entity);
        log.info("ResortRoomSpecialPrice updated with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse deleteSpecial(ResortRoomSpecialPriceEntity entity) {
        entity.setIsDeleted(true);
        entity.setIsActive(false);
        resortRoomSpecialPriceRepository.save(entity);
        log.info("ResortRoomSpecialPrice soft-deleted with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse deleteByCurrency(ResortRoomEntity resortRoomEntity, CurrencyEntity currencyEntity) {
        // Locked, and deliberately acquired BEFORE fetching this currency's own row set below — see
        // requireActiveMainPrice for why this contends on the same physical row a concurrent
        // createSpecial call locks.
        ResortRoomMainPriceEntity mainEntity = resortRoomMainPriceRepository
                .findForUpdate(resortRoomEntity.getId(), currencyEntity.getId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "No active price override found for currency id: " + currencyEntity.getId()));

        mainEntity.setIsDeleted(true);
        mainEntity.setIsActive(false);
        resortRoomMainPriceRepository.save(mainEntity);

        List<ResortRoomSpecialPriceEntity> specialEntities = resortRoomSpecialPriceRepository
                .findByResortRoomEntity_IdAndCurrencyEntity_IdAndIsActiveAndIsDeleted(
                        resortRoomEntity.getId(), currencyEntity.getId(), true, false);
        specialEntities.forEach(entity -> {
            entity.setIsDeleted(true);
            entity.setIsActive(false);
        });
        resortRoomSpecialPriceRepository.saveAll(specialEntities);

        log.info("ResortRoomPrice soft-deleted all overrides for resort room id: {}, currency id: {} "
                        + "(1 main, {} special) — currency now inherits from the room's category",
                resortRoomEntity.getId(), currencyEntity.getId(), specialEntities.size());
        return new SuccessResponse(true, currencyEntity.getId());
    }

    private ResortRoomMainPriceDto buildMainDto(ResortRoomMainPriceEntity entity,
                                                 List<ResortWeeklyScheduleDayDto> weekdayDays,
                                                 List<ResortWeeklyScheduleDayDto> weekendDays) {
        return ResortRoomPriceMapper.toDto(entity)
                .resortRoom(ResortRoomMapper.toDto(entity.getResortRoomEntity()).build())
                .priceUnit(PriceUnitMapper.toDto(entity.getPriceUnitEntity()).build())
                .currency(CurrencyMapper.toDto(entity.getCurrencyEntity()).build())
                .weekdayDays(weekdayDays)
                .weekendDays(weekendDays)
                .build();
    }

    private ResortRoomDateRangePriceDto buildSpecialDto(ResortRoomSpecialPriceEntity entity,
                                                         List<ResortWeeklyScheduleDayDto> weekdayDays,
                                                         List<ResortWeeklyScheduleDayDto> weekendDays) {
        return ResortRoomPriceMapper.toDto(entity)
                .resortRoom(ResortRoomMapper.toDto(entity.getResortRoomEntity()).build())
                .priceUnit(PriceUnitMapper.toDto(entity.getPriceUnitEntity()).build())
                .currency(CurrencyMapper.toDto(entity.getCurrencyEntity()).build())
                .weekdayDays(weekdayDays)
                .weekendDays(weekendDays)
                .build();
    }

    private List<ResortWeeklyScheduleDayDto> mapDays(List<ResortWeeklyScheduleDayEntity> scheduleDays) {
        return scheduleDays.stream()
                .map(dayEntity -> ResortWeeklyScheduleDayMapper.toDto(dayEntity)
                        .dayOfWeek(DayOfWeekMapper.toDto(dayEntity.getDayOfWeekEntity()).build())
                        .build())
                .toList();
    }
}
