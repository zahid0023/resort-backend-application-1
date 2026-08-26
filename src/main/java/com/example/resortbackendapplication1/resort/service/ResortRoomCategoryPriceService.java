package com.example.resortbackendapplication1.resort.service;

import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.currency.model.entity.CurrencyEntity;
import com.example.resortbackendapplication1.dayofweek.model.entity.DayOfWeekEntity;
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

import java.util.List;

public interface ResortRoomCategoryPriceService {

    /**
     * Adds a new currency's full BASE/WEEKDAY/WEEKEND price set to an already-existing resort room category —
     * the same bundling {@link com.example.resortbackendapplication1.resort.serviceImpl.ResortRoomCategoryServiceImpl}
     * does per currency at creation time, exposed here so additional currencies can be added afterward.
     */
    CreateResortRoomCategoryPriceGroupResponse createMain(CreateResortRoomCategoryMainPriceRequest request,
                                                           ResortRoomCategoryEntity resortRoomCategoryEntity,
                                                           PriceTypeEntity basePriceTypeEntity,
                                                           PriceTypeEntity weekdayPriceTypeEntity,
                                                           PriceTypeEntity weekendPriceTypeEntity,
                                                           CurrencyEntity currencyEntity,
                                                           PriceUnitEntity basePriceUnitEntity,
                                                           PriceUnitEntity weekdayPriceUnitEntity,
                                                           PriceUnitEntity weekendPriceUnitEntity,
                                                           List<DayOfWeekEntity> weekdayDayOfWeekEntities,
                                                           List<DayOfWeekEntity> weekendDayOfWeekEntities);

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

    ResortRoomCategoryPriceGroupResponse getAllGroupedByCurrency(Long resortRoomCategoryId, CurrencyEntity currencyEntity);

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
                               PriceUnitEntity weekendPriceUnitEntity,
                               List<DayOfWeekEntity> weekdayDayOfWeekEntities,
                               List<DayOfWeekEntity> weekendDayOfWeekEntities);

    SuccessResponse updateHoliday(ResortRoomCategoryPriceEntity entity,
                                  UpdateResortRoomCategoryHolidayPriceRequest request,
                                  PriceUnitEntity priceUnitEntity);

    SuccessResponse updateSpecial(ResortRoomCategoryPriceEntity entity,
                                  UpdateResortRoomCategorySpecialPriceRequest request,
                                  PriceUnitEntity priceUnitEntity);

    SuccessResponse delete(ResortRoomCategoryPriceEntity entity);
}
