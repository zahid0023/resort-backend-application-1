package com.example.resortbackendapplication1.resort.roomcategory.service;

import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.currency.model.entity.CurrencyEntity;
import com.example.resortbackendapplication1.price.model.entity.PriceUnitEntity;
import com.example.resortbackendapplication1.resort.roomcategory.dto.request.resortroomcategoryprice.CreateResortRoomCategoryMainPriceRequest;
import com.example.resortbackendapplication1.resort.roomcategory.dto.request.resortroomcategoryprice.CreateResortRoomCategorySpecialPriceRequest;
import com.example.resortbackendapplication1.resort.roomcategory.dto.request.resortroomcategoryprice.UpdateResortRoomCategoryMainPriceRequest;
import com.example.resortbackendapplication1.resort.roomcategory.dto.request.resortroomcategoryprice.UpdateResortRoomCategorySpecialPriceRequest;
import com.example.resortbackendapplication1.resort.roomcategory.dto.response.resortroomcategoryprices.ResortRoomCategoryPriceCountResponse;
import com.example.resortbackendapplication1.resort.roomcategory.dto.response.resortroomcategoryprices.ResortRoomCategoryPriceGroupResponse;
import com.example.resortbackendapplication1.resort.roomcategory.model.entity.ResortRoomCategoryEntity;
import com.example.resortbackendapplication1.resort.roomcategory.model.entity.ResortRoomCategorySpecialPriceEntity;
import com.example.resortbackendapplication1.resort.core.model.entity.ResortWeeklyScheduleDayEntity;

import java.util.List;

/**
 * Coordinates both resort room category price tables (main/special) — an intentional exception to "a
 * ServiceImpl only touches its own repository", since the two are one feature split across tables for schema
 * reasons, not separate domains (mirrors the shape this service already had when they were one table). A
 * holiday is not a separate concept — it's just a special price whose {@code name} says so (e.g. "Eid-ul-Fitr").
 */
public interface ResortRoomCategoryPriceService {

    /**
     * Adds a new currency's main (base/weekday/weekend) price to an already-existing resort room category —
     * the same row {@code ResortRoomCategoryServiceImpl} attaches per currency at creation time, exposed here
     * so additional currencies can be added afterward. Which days count as WEEKDAY/WEEKEND is not part of this
     * call — it's shared by every currency at the resort, set separately via
     * {@code ResortWeeklyScheduleService.updateWeeklySchedule}.
     */
    SuccessResponse createMain(CreateResortRoomCategoryMainPriceRequest request,
                               ResortRoomCategoryEntity resortRoomCategoryEntity,
                               CurrencyEntity currencyEntity,
                               PriceUnitEntity priceUnitEntity);

    SuccessResponse createSpecial(CreateResortRoomCategorySpecialPriceRequest request,
                                  ResortRoomCategoryEntity resortRoomCategoryEntity,
                                  CurrencyEntity currencyEntity,
                                  PriceUnitEntity priceUnitEntity);

    ResortRoomCategorySpecialPriceEntity getSpecialEntityById(Long resortRoomCategoryId, Long id);

    /**
     * {@code weekdayScheduleDays}/{@code weekendScheduleDays} are the resort's current weekly schedule rows
     * (resolved by the controller via {@code ResortWeeklyScheduleService}), embedded as each row's
     * {@code weekday_days}/{@code weekend_days} — passed in rather than looked up here since a ServiceImpl
     * must never call another domain's Service.
     */
    ResortRoomCategoryPriceGroupResponse getAllGroupedByCurrency(Long resortRoomCategoryId, CurrencyEntity currencyEntity,
                                                                  List<ResortWeeklyScheduleDayEntity> weekdayScheduleDays,
                                                                  List<ResortWeeklyScheduleDayEntity> weekendScheduleDays);

    /**
     * Counts the distinct currencies for which this resort room category has an active main price.
     */
    ResortRoomCategoryPriceCountResponse getCount(Long resortRoomCategoryId);

    /** Updates an already-existing currency's main price row in place. */
    SuccessResponse updateMain(ResortRoomCategoryEntity resortRoomCategoryEntity,
                               CurrencyEntity currencyEntity,
                               UpdateResortRoomCategoryMainPriceRequest request,
                               PriceUnitEntity priceUnitEntity);

    SuccessResponse updateSpecial(ResortRoomCategorySpecialPriceEntity entity,
                                  UpdateResortRoomCategorySpecialPriceRequest request,
                                  PriceUnitEntity priceUnitEntity);

    SuccessResponse deleteSpecial(ResortRoomCategorySpecialPriceEntity entity);

    /**
     * Soft-deletes every active price row for one currency — main plus any special rows — atomically. This is
     * the only way to remove a currency's main price; deleting a currency's main price any other way would
     * orphan that currency's special rows (they require an active main price to exist). Refuses to delete the
     * room category's last remaining currency — every resort room category must keep at least one currency's
     * prices.
     */
    SuccessResponse deleteByCurrency(ResortRoomCategoryEntity resortRoomCategoryEntity, CurrencyEntity currencyEntity);
}
