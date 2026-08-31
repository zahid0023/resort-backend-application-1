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
import com.example.resortbackendapplication1.resort.room.model.entity.ResortRoomMainPriceEntity;
import com.example.resortbackendapplication1.resort.room.model.entity.ResortRoomSpecialPriceEntity;
import com.example.resortbackendapplication1.resort.core.model.entity.ResortWeeklyScheduleDayEntity;

import java.util.List;
import java.util.Optional;

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

    /**
     * A Special override no longer requires the room's own Main override — it only needs *some* main price to
     * be resolvable for this currency, the room's own or its category's. {@code categoryHasActiveMain} is
     * resolved by the controller via {@code ResortRoomCategoryPriceService.hasActiveMain}, never fetched here —
     * a ServiceImpl must never call another entity's Service. Fails with {@code 404 ENTITY_NOT_FOUND} only when
     * neither the room nor its category has an active main price for this currency.
     */
    SuccessResponse createSpecial(CreateResortRoomSpecialPriceRequest request,
                                  ResortRoomEntity resortRoomEntity,
                                  CurrencyEntity currencyEntity,
                                  PriceUnitEntity priceUnitEntity,
                                  boolean categoryHasActiveMain);

    ResortRoomSpecialPriceEntity getSpecialEntityById(Long resortRoomId, Long id);

    /**
     * The room's own active main price override for this currency, if it has one — no category fallback here
     * (the caller, e.g. ResortBookingController computing a reservation's total_price, resolves that itself via
     * ResortRoomCategoryPriceService, mirroring the mainInherited logic in getAllGroupedByCurrency above).
     */
    Optional<ResortRoomMainPriceEntity> getMainEntityByCurrency(Long resortRoomId, Long currencyId);

    /** The room's own active special price overrides for this currency, if any — no category fallback here. */
    List<ResortRoomSpecialPriceEntity> getSpecialEntitiesByCurrency(Long resortRoomId, Long currencyId);

    /**
     * Main and Specials are resolved independently — a room can override just one, both, or neither, for the
     * same currency (see {@code ResortRoomPriceGroupDto}). Whichever side the room has no active override rows
     * for falls back to {@code categoryFallback} (the room's category's already-resolved bundle for this
     * currency, from {@code ResortRoomCategoryPriceService.getAllGroupedByCurrency}), marked
     * {@code mainInherited}/{@code specialsInherited} accordingly. {@code categoryFallback} is resolved by the
     * controller, never fetched here — a ServiceImpl must never call another entity's Service.
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
     * Soft-deletes every active override row for one currency — main and/or special, whichever the room
     * actually has — atomically, reverting the room back to inheriting that currency's price from its category.
     * Since Main and Specials are independent, a room may have only one side overridden (e.g. Specials with no
     * Main override, inheriting Main from the category); this deletes whichever side(s) are present and fails
     * with {@code 404 ENTITY_NOT_FOUND} only if neither side has any active row for this currency. Unlike the
     * category-level equivalent, there is no "at least one currency must remain" guard: a room is allowed to
     * have zero overrides (fully inherited).
     */
    SuccessResponse deleteByCurrency(ResortRoomEntity resortRoomEntity, CurrencyEntity currencyEntity);
}
