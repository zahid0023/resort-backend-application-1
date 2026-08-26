package com.example.resortbackendapplication1.resort.service;

import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.currency.model.entity.CurrencyEntity;
import com.example.resortbackendapplication1.price.model.entity.PriceTypeEntity;
import com.example.resortbackendapplication1.price.model.entity.PriceUnitEntity;
import com.example.resortbackendapplication1.resort.dto.request.resortroomcategoryprice.CreateResortRoomCategoryHolidayPriceRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortroomcategoryprice.CreateResortRoomCategoryMainPriceRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortroomcategoryprice.CreateResortRoomCategorySpecialPriceRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortroomcategoryprice.UpdateResortRoomCategoryHolidayPriceRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortroomcategoryprice.UpdateResortRoomCategoryMainPriceRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortroomcategoryprice.UpdateResortRoomCategorySpecialPriceRequest;
import com.example.resortbackendapplication1.resort.dto.response.resortroomcategoryprices.CreateResortRoomCategoryPriceGroupResponse;
import com.example.resortbackendapplication1.resort.dto.response.resortroomcategoryprices.ResortRoomCategoryPriceCountResponse;
import com.example.resortbackendapplication1.resort.dto.response.resortroomcategoryprices.ResortRoomCategoryPriceGroupResponse;
import com.example.resortbackendapplication1.resort.model.entity.ResortRoomCategoryEntity;
import com.example.resortbackendapplication1.resort.model.entity.ResortRoomCategoryPriceEntity;
import com.example.resortbackendapplication1.resort.model.entity.ResortWeeklyScheduleDayEntity;

import java.util.List;

public interface ResortRoomCategoryPriceService {

    /**
     * Adds a new currency's full BASE/WEEKDAY/WEEKEND price set to an already-existing resort room category —
     * the same bundling {@link com.example.resortbackendapplication1.resort.serviceImpl.ResortRoomCategoryServiceImpl}
     * does per currency at creation time, exposed here so additional currencies can be added afterward. Which
     * days count as WEEKDAY/WEEKEND is not part of this call — it's shared by every currency at the resort,
     * set separately via {@code ResortWeeklyScheduleService.updateWeeklySchedule}.
     */
    CreateResortRoomCategoryPriceGroupResponse createMain(CreateResortRoomCategoryMainPriceRequest request,
                                                           ResortRoomCategoryEntity resortRoomCategoryEntity,
                                                           PriceTypeEntity basePriceTypeEntity,
                                                           PriceTypeEntity weekdayPriceTypeEntity,
                                                           PriceTypeEntity weekendPriceTypeEntity,
                                                           CurrencyEntity currencyEntity,
                                                           PriceUnitEntity basePriceUnitEntity,
                                                           PriceUnitEntity weekdayPriceUnitEntity,
                                                           PriceUnitEntity weekendPriceUnitEntity);

    SuccessResponse createHoliday(CreateResortRoomCategoryHolidayPriceRequest request,
                                  ResortRoomCategoryEntity resortRoomCategoryEntity,
                                  PriceTypeEntity holidayPriceTypeEntity,
                                  PriceUnitEntity priceUnitEntity,
                                  CurrencyEntity currencyEntity);

    SuccessResponse createSpecial(CreateResortRoomCategorySpecialPriceRequest request,
                                  ResortRoomCategoryEntity resortRoomCategoryEntity,
                                  PriceTypeEntity specialPriceTypeEntity,
                                  PriceUnitEntity priceUnitEntity,
                                  CurrencyEntity currencyEntity);

    ResortRoomCategoryPriceEntity getEntityById(Long resortRoomCategoryId, Long id);

    /**
     * {@code weekdayScheduleDays}/{@code weekendScheduleDays} are the resort's current weekly schedule rows
     * (resolved by the controller via {@code ResortWeeklyScheduleService}), embedded as each WEEKDAY/WEEKEND
     * row's {@code days} — passed in rather than looked up here since a ServiceImpl must never call another
     * domain's Service.
     */
    ResortRoomCategoryPriceGroupResponse getAllGroupedByCurrency(Long resortRoomCategoryId, CurrencyEntity currencyEntity,
                                                                  List<ResortWeeklyScheduleDayEntity> weekdayScheduleDays,
                                                                  List<ResortWeeklyScheduleDayEntity> weekendScheduleDays);

    /**
     * Counts the distinct currencies for which this resort room category has an active main (`BAS`) price set —
     * `BAS`/`WKD`/`WKE` are always created/replaced together, so `BAS` alone is a reliable signal.
     */
    ResortRoomCategoryPriceCountResponse getCount(Long resortRoomCategoryId);

    /**
     * Replaces an already-existing currency's BASE/WEEKDAY/WEEKEND price set — the three old rows (looked up
     * by {@code resortRoomCategoryEntity}/{@code currencyEntity}, since a client only knows the currency, not
     * the three individual row ids) are soft-deleted and three brand-new rows are created in their place,
     * mirroring {@code ResortFacilityOperatingHoursServiceImpl.setWeeklySchedule}.
     */
    SuccessResponse updateMain(ResortRoomCategoryEntity resortRoomCategoryEntity,
                               CurrencyEntity currencyEntity,
                               UpdateResortRoomCategoryMainPriceRequest request,
                               PriceTypeEntity basePriceTypeEntity,
                               PriceTypeEntity weekdayPriceTypeEntity,
                               PriceTypeEntity weekendPriceTypeEntity,
                               PriceUnitEntity basePriceUnitEntity,
                               PriceUnitEntity weekdayPriceUnitEntity,
                               PriceUnitEntity weekendPriceUnitEntity);

    SuccessResponse updateHoliday(ResortRoomCategoryPriceEntity entity,
                                  UpdateResortRoomCategoryHolidayPriceRequest request,
                                  PriceUnitEntity priceUnitEntity);

    SuccessResponse updateSpecial(ResortRoomCategoryPriceEntity entity,
                                  UpdateResortRoomCategorySpecialPriceRequest request,
                                  PriceUnitEntity priceUnitEntity);

    SuccessResponse delete(ResortRoomCategoryPriceEntity entity);

    /**
     * Soft-deletes every active price row for one currency — BASE/WEEKDAY/WEEKEND plus any HOLIDAY/SPECIAL
     * rows — atomically. This is the only way to remove a currency's BASE/WEEKDAY/WEEKEND rows;
     * {@link #delete} refuses them individually because deleting just the main set would orphan that
     * currency's HOLIDAY/SPECIAL rows (they require an active BASE price to exist).
     * Refuses to delete the room category's last remaining currency — every resort room category must keep
     * at least one currency's prices.
     */
    SuccessResponse deleteByCurrency(ResortRoomCategoryEntity resortRoomCategoryEntity, CurrencyEntity currencyEntity);
}
