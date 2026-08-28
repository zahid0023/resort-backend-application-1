package com.example.resortbackendapplication1.resort.roomcategory.serviceImpl;

import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.currency.model.entity.CurrencyEntity;
import com.example.resortbackendapplication1.currency.model.mapper.CurrencyMapper;
import com.example.resortbackendapplication1.dayofweek.model.mapper.DayOfWeekMapper;
import com.example.resortbackendapplication1.price.model.entity.PriceUnitEntity;
import com.example.resortbackendapplication1.price.model.mapper.PriceUnitMapper;
import com.example.resortbackendapplication1.resort.roomcategory.dto.request.resortroomcategoryprice.CreateResortRoomCategoryMainPriceRequest;
import com.example.resortbackendapplication1.resort.roomcategory.dto.request.resortroomcategoryprice.CreateResortRoomCategorySpecialPriceRequest;
import com.example.resortbackendapplication1.resort.roomcategory.dto.request.resortroomcategoryprice.UpdateResortRoomCategoryMainPriceRequest;
import com.example.resortbackendapplication1.resort.roomcategory.dto.request.resortroomcategoryprice.UpdateResortRoomCategorySpecialPriceRequest;
import com.example.resortbackendapplication1.resort.roomcategory.dto.response.resortroomcategoryprices.ResortRoomCategoryPriceCountResponse;
import com.example.resortbackendapplication1.resort.roomcategory.dto.response.resortroomcategoryprices.ResortRoomCategoryPriceGroupResponse;
import com.example.resortbackendapplication1.resort.roomcategory.model.dto.ResortRoomCategoryDateRangePriceDto;
import com.example.resortbackendapplication1.resort.roomcategory.model.dto.ResortRoomCategoryMainPriceDto;
import com.example.resortbackendapplication1.resort.roomcategory.model.dto.ResortRoomCategoryPriceGroupDto;
import com.example.resortbackendapplication1.resort.core.model.dto.ResortWeeklyScheduleDayDto;
import com.example.resortbackendapplication1.resort.roomcategory.model.entity.ResortRoomCategoryEntity;
import com.example.resortbackendapplication1.resort.roomcategory.model.entity.ResortRoomCategoryMainPriceEntity;
import com.example.resortbackendapplication1.resort.roomcategory.model.entity.ResortRoomCategorySpecialPriceEntity;
import com.example.resortbackendapplication1.resort.core.model.entity.ResortWeeklyScheduleDayEntity;
import com.example.resortbackendapplication1.resort.roomcategory.model.mapper.ResortRoomCategoryMapper;
import com.example.resortbackendapplication1.resort.roomcategory.model.mapper.ResortRoomCategoryPriceMapper;
import com.example.resortbackendapplication1.resort.core.model.mapper.ResortWeeklyScheduleDayMapper;
import com.example.resortbackendapplication1.resort.roomcategory.repository.ResortRoomCategoryMainPriceRepository;
import com.example.resortbackendapplication1.resort.roomcategory.repository.ResortRoomCategorySpecialPriceRepository;
import com.example.resortbackendapplication1.resort.roomcategory.service.ResortRoomCategoryPriceService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class ResortRoomCategoryPriceServiceImpl implements ResortRoomCategoryPriceService {

    private final ResortRoomCategoryMainPriceRepository resortRoomCategoryMainPriceRepository;
    private final ResortRoomCategorySpecialPriceRepository resortRoomCategorySpecialPriceRepository;

    public ResortRoomCategoryPriceServiceImpl(ResortRoomCategoryMainPriceRepository resortRoomCategoryMainPriceRepository,
                                              ResortRoomCategorySpecialPriceRepository resortRoomCategorySpecialPriceRepository) {
        this.resortRoomCategoryMainPriceRepository = resortRoomCategoryMainPriceRepository;
        this.resortRoomCategorySpecialPriceRepository = resortRoomCategorySpecialPriceRepository;
    }

    @Transactional
    @Override
    public SuccessResponse createMain(CreateResortRoomCategoryMainPriceRequest request,
                                      ResortRoomCategoryEntity resortRoomCategoryEntity,
                                      CurrencyEntity currencyEntity,
                                      PriceUnitEntity priceUnitEntity) {
        if (resortRoomCategoryMainPriceRepository
                .findByResortRoomCategoryEntity_IdAndCurrencyEntity_IdAndIsActiveAndIsDeleted(
                        resortRoomCategoryEntity.getId(), currencyEntity.getId(), true, false)
                .isPresent()) {
            throw new IllegalStateException(
                    "This room category already has a main price for currency id: " + currencyEntity.getId());
        }
        ResortRoomCategoryMainPriceEntity entity = ResortRoomCategoryPriceMapper.createMain(
                request, resortRoomCategoryEntity, priceUnitEntity, currencyEntity);
        resortRoomCategoryMainPriceRepository.save(entity);
        log.info("ResortRoomCategoryMainPrice created for resort room category id: {}, currency id: {}, id: {}",
                resortRoomCategoryEntity.getId(), currencyEntity.getId(), entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse createSpecial(CreateResortRoomCategorySpecialPriceRequest request,
                                         ResortRoomCategoryEntity resortRoomCategoryEntity,
                                         CurrencyEntity currencyEntity,
                                         PriceUnitEntity priceUnitEntity) {
        requireActiveMainPrice(resortRoomCategoryEntity, currencyEntity);
        ResortRoomCategorySpecialPriceEntity entity = ResortRoomCategoryPriceMapper.createSpecial(
                request, resortRoomCategoryEntity, priceUnitEntity, currencyEntity);
        resortRoomCategorySpecialPriceRepository.save(entity);
        log.info("ResortRoomCategorySpecialPrice created with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    /**
     * Locked (not the plain findByResortRoomCategoryEntity_Id...CurrencyEntity_Id... lookup) so this contends
     * on the same physical row {@code deleteByCurrency} locks via {@code findActiveForUpdate} — closes the
     * write-skew race where this check and a concurrent {@code deleteByCurrency} call for the same currency
     * could each read a stale, independent snapshot and both proceed, orphaning a freshly created row.
     */
    private void requireActiveMainPrice(ResortRoomCategoryEntity resortRoomCategoryEntity, CurrencyEntity currencyEntity) {
        if (resortRoomCategoryMainPriceRepository
                .findForUpdate(resortRoomCategoryEntity.getId(), currencyEntity.getId())
                .isEmpty()) {
            throw new EntityNotFoundException(
                    "This room category has no active main price for currency id: " + currencyEntity.getId()
                            + " — create a main price for this currency first");
        }
    }

    @Override
    public ResortRoomCategorySpecialPriceEntity getSpecialEntityById(Long resortRoomCategoryId, Long id) {
        return resortRoomCategorySpecialPriceRepository
                .findByResortRoomCategoryEntity_IdAndIdAndIsActiveAndIsDeleted(resortRoomCategoryId, id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("ResortRoomCategorySpecialPrice not found with id: " + id));
    }

    @Override
    public ResortRoomCategoryPriceGroupResponse getAllGroupedByCurrency(Long resortRoomCategoryId, CurrencyEntity currencyEntity,
                                                                        List<ResortWeeklyScheduleDayEntity> weekdayScheduleDays,
                                                                        List<ResortWeeklyScheduleDayEntity> weekendScheduleDays) {
        List<ResortWeeklyScheduleDayDto> weekdayDays = mapDays(weekdayScheduleDays);
        List<ResortWeeklyScheduleDayDto> weekendDays = mapDays(weekendScheduleDays);

        ResortRoomCategoryMainPriceDto mainDto = resortRoomCategoryMainPriceRepository
                .findByResortRoomCategoryEntity_IdAndCurrencyEntity_IdAndIsActiveAndIsDeleted(
                        resortRoomCategoryId, currencyEntity.getId(), true, false)
                .map(entity -> buildMainDto(entity, weekdayDays, weekendDays))
                .orElse(null);

        List<ResortRoomCategoryDateRangePriceDto> specials = resortRoomCategorySpecialPriceRepository
                .findByResortRoomCategoryEntity_IdAndCurrencyEntity_IdAndIsActiveAndIsDeleted(
                        resortRoomCategoryId, currencyEntity.getId(), true, false)
                .stream()
                .map(entity -> buildSpecialDto(entity, weekdayDays, weekendDays))
                .toList();

        ResortRoomCategoryPriceGroupDto groupDto = ResortRoomCategoryPriceGroupDto.builder()
                .currency(CurrencyMapper.toDto(currencyEntity).build())
                .main(mainDto)
                .specials(specials)
                .build();
        return new ResortRoomCategoryPriceGroupResponse(groupDto);
    }

    @Override
    public ResortRoomCategoryPriceCountResponse getCount(Long resortRoomCategoryId) {
        List<String> codes = resortRoomCategoryMainPriceRepository
                .findDistinctCurrencyCodeByResortRoomCategoryEntity_IdAndIsActiveAndIsDeleted(
                        resortRoomCategoryId, true, false);
        return new ResortRoomCategoryPriceCountResponse((long) codes.size(), codes);
    }

    @Transactional
    @Override
    public SuccessResponse updateMain(ResortRoomCategoryEntity resortRoomCategoryEntity,
                                      CurrencyEntity currencyEntity,
                                      UpdateResortRoomCategoryMainPriceRequest request,
                                      PriceUnitEntity priceUnitEntity) {
        ResortRoomCategoryMainPriceEntity entity = resortRoomCategoryMainPriceRepository
                .findByResortRoomCategoryEntity_IdAndCurrencyEntity_IdAndIsActiveAndIsDeleted(
                        resortRoomCategoryEntity.getId(), currencyEntity.getId(), true, false)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Main price not found for currency id: " + currencyEntity.getId()));
        ResortRoomCategoryPriceMapper.updateMain(entity, request, priceUnitEntity);
        resortRoomCategoryMainPriceRepository.save(entity);
        log.info("ResortRoomCategoryMainPrice updated for resort room category id: {}, currency id: {}, id: {}",
                resortRoomCategoryEntity.getId(), currencyEntity.getId(), entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse updateSpecial(ResortRoomCategorySpecialPriceEntity entity,
                                         UpdateResortRoomCategorySpecialPriceRequest request,
                                         PriceUnitEntity priceUnitEntity) {
        ResortRoomCategoryPriceMapper.updateSpecial(entity, request, priceUnitEntity);
        resortRoomCategorySpecialPriceRepository.save(entity);
        log.info("ResortRoomCategorySpecialPrice updated with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse deleteSpecial(ResortRoomCategorySpecialPriceEntity entity) {
        entity.setIsDeleted(true);
        entity.setIsActive(false);
        resortRoomCategorySpecialPriceRepository.save(entity);
        log.info("ResortRoomCategorySpecialPrice soft-deleted with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse deleteByCurrency(ResortRoomCategoryEntity resortRoomCategoryEntity, CurrencyEntity currencyEntity) {
        // Locked, and deliberately acquired BEFORE fetching this currency's own row set below — a concurrent
        // createSpecial call for this same currency locks the same physical main-price row (see
        // requireActiveMainPrice), so this either blocks until that call commits (and then the fetch below
        // picks up its freshly created row too) or forces that call to block until this delete commits (and
        // then it correctly finds no active main price left).
        List<ResortRoomCategoryMainPriceEntity> activeMainRows = resortRoomCategoryMainPriceRepository
                .findActiveForUpdate(resortRoomCategoryEntity.getId());

        ResortRoomCategoryMainPriceEntity mainEntity = activeMainRows.stream()
                .filter(row -> row.getCurrencyEntity().getId().equals(currencyEntity.getId()))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException(
                        "No active prices found for currency id: " + currencyEntity.getId()));

        if (activeMainRows.size() <= 1) {
            throw new IllegalStateException(
                    "At least one currency's prices must remain for this room category — cannot delete the last currency.");
        }

        mainEntity.setIsDeleted(true);
        mainEntity.setIsActive(false);
        resortRoomCategoryMainPriceRepository.save(mainEntity);

        List<ResortRoomCategorySpecialPriceEntity> specialEntities = resortRoomCategorySpecialPriceRepository
                .findByResortRoomCategoryEntity_IdAndCurrencyEntity_IdAndIsActiveAndIsDeleted(
                        resortRoomCategoryEntity.getId(), currencyEntity.getId(), true, false);
        specialEntities.forEach(entity -> {
            entity.setIsDeleted(true);
            entity.setIsActive(false);
        });
        resortRoomCategorySpecialPriceRepository.saveAll(specialEntities);

        log.info("ResortRoomCategoryPrice soft-deleted all prices for resort room category id: {}, currency id: {} "
                        + "(1 main, {} special)",
                resortRoomCategoryEntity.getId(), currencyEntity.getId(), specialEntities.size());
        return new SuccessResponse(true, currencyEntity.getId());
    }

    private ResortRoomCategoryMainPriceDto buildMainDto(ResortRoomCategoryMainPriceEntity entity,
                                                          List<ResortWeeklyScheduleDayDto> weekdayDays,
                                                          List<ResortWeeklyScheduleDayDto> weekendDays) {
        return ResortRoomCategoryPriceMapper.toDto(entity)
                .resortRoomCategory(ResortRoomCategoryMapper.toDto(entity.getResortRoomCategoryEntity()).build())
                .priceUnit(PriceUnitMapper.toDto(entity.getPriceUnitEntity()).build())
                .currency(CurrencyMapper.toDto(entity.getCurrencyEntity()).build())
                .weekdayDays(weekdayDays)
                .weekendDays(weekendDays)
                .build();
    }

    private ResortRoomCategoryDateRangePriceDto buildSpecialDto(ResortRoomCategorySpecialPriceEntity entity,
                                                                  List<ResortWeeklyScheduleDayDto> weekdayDays,
                                                                  List<ResortWeeklyScheduleDayDto> weekendDays) {
        return ResortRoomCategoryPriceMapper.toDto(entity)
                .resortRoomCategory(ResortRoomCategoryMapper.toDto(entity.getResortRoomCategoryEntity()).build())
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
