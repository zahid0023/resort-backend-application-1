package com.example.resortbackendapplication1.resort.room.service;

import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.currency.model.entity.CurrencyEntity;
import com.example.resortbackendapplication1.price.model.entity.PriceUnitEntity;
import com.example.resortbackendapplication1.resort.room.dto.request.resortroomprice.CreateResortRoomMainPriceRequest;
import com.example.resortbackendapplication1.resort.room.dto.request.resortroomprice.CreateResortRoomSpecialPriceRequest;
import com.example.resortbackendapplication1.resort.room.dto.request.resortroomprice.UpdateResortRoomMainPriceRequest;
import com.example.resortbackendapplication1.resort.room.dto.request.resortroomprice.UpdateResortRoomSpecialPriceRequest;
import com.example.resortbackendapplication1.resort.room.dto.response.resortroomprices.ResortRoomPriceCountResponse;
import com.example.resortbackendapplication1.resort.room.dto.response.resortroomprices.ResortRoomPriceGroupResponse;
import com.example.resortbackendapplication1.resort.roomcategory.model.dto.ResortRoomCategoryPriceGroupDto;
import com.example.resortbackendapplication1.resort.room.model.entity.ResortRoomEntity;
import com.example.resortbackendapplication1.resort.room.model.entity.ResortRoomSpecialPriceEntity;
import com.example.resortbackendapplication1.resort.core.model.entity.ResortWeeklyScheduleDayEntity;

import java.util.List;

/**
 * Coordinates both resort room price tables (main/special) — an intentional exception to "a ServiceImpl only
 * touches its own repository", mirroring {@code ResortRoomCategoryPriceService}. Every row here is an optional
 * per-currency override of the room's category price — see the entities' javadoc. A holiday is not a separate
 * concept — it's just a special price whose {@code name} says so (e.g. "Eid-ul-Fitr").
 */
public interface ResortRoomPriceService {

    /**
     * Adds a currency's main price override to a resort room. Fails with {@code 409 CONFLICT} if the room
     * already has an active override for this currency.
     */
    SuccessResponse createMain(CreateResortRoomMainPriceRequest request,
                               ResortRoomEntity resortRoomEntity,
                               CurrencyEntity currencyEntity,
                               PriceUnitEntity priceUnitEntity);

    SuccessResponse createSpecial(CreateResortRoomSpecialPriceRequest request,
                                  ResortRoomEntity resortRoomEntity,
                                  CurrencyEntity currencyEntity,
                                  PriceUnitEntity priceUnitEntity);

    ResortRoomSpecialPriceEntity getSpecialEntityById(Long resortRoomId, Long id);

    /**
     * If the room has no active main price override for {@code currencyEntity}, {@code categoryFallback} (the
     * room's category's already-resolved bundle for this currency, from
     * {@code ResortRoomCategoryPriceService.getAllGroupedByCurrency}) is returned instead, marked
     * {@code inherited=true}. {@code categoryFallback} is resolved by the controller, never fetched here — a
     * ServiceImpl must never call another entity's Service.
     */
    ResortRoomPriceGroupResponse getAllGroupedByCurrency(Long resortRoomId, CurrencyEntity currencyEntity,
                                                          List<ResortWeeklyScheduleDayEntity> weekdayScheduleDays,
                                                          List<ResortWeeklyScheduleDayEntity> weekendScheduleDays,
                                                          ResortRoomCategoryPriceGroupDto categoryFallback);

    /** Counts the distinct currencies for which this resort room has its own active main price override. */
    ResortRoomPriceCountResponse getCount(Long resortRoomId);

    /** Updates an already-existing currency's main price override row in place. */
    SuccessResponse updateMain(ResortRoomEntity resortRoomEntity,
                               CurrencyEntity currencyEntity,
                               UpdateResortRoomMainPriceRequest request,
                               PriceUnitEntity priceUnitEntity);

    SuccessResponse updateSpecial(ResortRoomSpecialPriceEntity entity,
                                  UpdateResortRoomSpecialPriceRequest request,
                                  PriceUnitEntity priceUnitEntity);

    SuccessResponse deleteSpecial(ResortRoomSpecialPriceEntity entity);

    /**
     * Soft-deletes every active override row for one currency — main plus any special — atomically, reverting
     * the room back to inheriting that currency's price from its category. Unlike the category-level
     * equivalent, there is no "at least one currency must remain" guard: a room is allowed to have zero
     * overrides (fully inherited).
     */
    SuccessResponse deleteByCurrency(ResortRoomEntity resortRoomEntity, CurrencyEntity currencyEntity);
}
