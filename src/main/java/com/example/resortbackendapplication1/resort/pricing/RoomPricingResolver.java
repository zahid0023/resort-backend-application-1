package com.example.resortbackendapplication1.resort.pricing;

import com.example.resortbackendapplication1.price.model.entity.PriceUnitEntity;
import com.example.resortbackendapplication1.resort.core.model.entity.ResortEntity;
import com.example.resortbackendapplication1.resort.core.model.entity.ResortWeeklyScheduleDayEntity;
import com.example.resortbackendapplication1.resort.core.model.enums.DayType;
import com.example.resortbackendapplication1.resort.core.service.ResortWeeklyScheduleService;
import com.example.resortbackendapplication1.resort.room.model.entity.ResortRoomEntity;
import com.example.resortbackendapplication1.resort.room.model.entity.ResortRoomMainPriceEntity;
import com.example.resortbackendapplication1.resort.room.model.entity.ResortRoomSpecialPriceEntity;
import com.example.resortbackendapplication1.resort.room.service.ResortRoomPriceService;
import com.example.resortbackendapplication1.resort.roomcategory.model.entity.ResortRoomCategoryMainPriceEntity;
import com.example.resortbackendapplication1.resort.roomcategory.service.ResortRoomCategoryPriceService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The single "what does this room cost for this stay" resolver — any caller that needs a priced quote for a
 * resort room over a date range (booking a reservation, pricing an availability search result, a future
 * quote/estimate endpoint, ...) depends on this, and only this, rather than re-deriving Main/Special
 * inheritance itself. Lives in its own {@code resort.pricing} package, independent of any one consumer
 * (booking, reservation, availability), so new callers can depend on it without pulling in a consumer module
 * they don't need — the only domains it itself depends on are Room/RoomCategory pricing and the resort's
 * weekly schedule, which are its inherent inputs, not a consumer coupling.
 *
 * <p>A plain bean, not a ServiceImpl, since it legitimately needs to call three different domains' Services
 * (ResortRoomPriceService, ResortRoomCategoryPriceService, ResortWeeklyScheduleService), which a ServiceImpl
 * must never do. The three constructor dependencies are ordinary Spring-managed interfaces, so this class is
 * trivially unit-testable in isolation with mocks/fakes for each — no repository, no persistence context, no
 * web layer involved.
 */
@Component
public class RoomPricingResolver {

    private final ResortRoomPriceService resortRoomPriceService;
    private final ResortRoomCategoryPriceService resortRoomCategoryPriceService;
    private final ResortWeeklyScheduleService resortWeeklyScheduleService;

    public RoomPricingResolver(ResortRoomPriceService resortRoomPriceService,
                               ResortRoomCategoryPriceService resortRoomCategoryPriceService,
                               ResortWeeklyScheduleService resortWeeklyScheduleService) {
        this.resortRoomPriceService = resortRoomPriceService;
        this.resortRoomCategoryPriceService = resortRoomCategoryPriceService;
        this.resortWeeklyScheduleService = resortWeeklyScheduleService;
    }

    public record Result(PriceUnitEntity priceUnitEntity, BigDecimal totalPrice) {
    }

    public record NightlyResult(PriceUnitEntity priceUnitEntity,
                                List<PricingCalculator.NightlyRate> nights, BigDecimal totalPrice) {
    }

    public Result resolve(ResortEntity resortEntity, ResortRoomEntity resortRoomEntity, Long resortRoomCategoryId,
                          Long currencyId, LocalDate checkIn, LocalDate checkOut) {
        NightlyResult nightlyResult = resolveNightly(resortEntity, resortRoomEntity, resortRoomCategoryId,
                currencyId, checkIn, checkOut);
        return new Result(nightlyResult.priceUnitEntity(), nightlyResult.totalPrice());
    }

    /**
     * Same resolution as {@link #resolve}, broken out night by night instead of summed. Throws when the room
     * (and its category) has no resolvable Main price for the currency — the right behavior for a caller like
     * {@code BookingController} where an unpriceable room at reservation-creation time is a genuine error.
     */
    public NightlyResult resolveNightly(ResortEntity resortEntity, ResortRoomEntity resortRoomEntity, Long resortRoomCategoryId,
                                        Long currencyId, LocalDate checkIn, LocalDate checkOut) {
        return tryResolveNightly(resortEntity, resortRoomEntity, resortRoomCategoryId, currencyId, checkIn, checkOut)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No active main price resolvable for currency id: " + currencyId
                                + " — this room has none of its own, and its category has none either"));
    }

    /**
     * Same resolution as {@link #resolveNightly}, but reports an unpriceable room as an empty result instead
     * of throwing — for a caller like {@code AvailabilityController} where "no price for this currency" is a
     * normal, expected outcome for some rooms, not an exceptional one, and pricing (not the caller) is what
     * decides whether a room counts as priceable.
     */
    public Optional<NightlyResult> tryResolveNightly(ResortEntity resortEntity, ResortRoomEntity resortRoomEntity,
                                                      Long resortRoomCategoryId, Long currencyId,
                                                      LocalDate checkIn, LocalDate checkOut) {
        Set<DayOfWeek> weekendDays = resolveWeekendDays(resortEntity);
        return tryResolvePricingInputs(resortRoomEntity, resortRoomCategoryId, currencyId)
                .map(pricingInputs -> {
                    List<PricingCalculator.NightlyRate> nights = PricingCalculator.calculateNightly(
                            checkIn, checkOut, weekendDays, pricingInputs.weekdayPrice(), pricingInputs.weekendPrice(),
                            pricingInputs.specialWindows());
                    BigDecimal totalPrice = nights.stream()
                            .map(PricingCalculator.NightlyRate::price)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    return new NightlyResult(pricingInputs.priceUnitEntity(), nights, totalPrice);
                });
    }

    private Set<DayOfWeek> resolveWeekendDays(ResortEntity resortEntity) {
        List<ResortWeeklyScheduleDayEntity> weekendScheduleDays =
                resortWeeklyScheduleService.getEntitiesByDayType(resortEntity, DayType.WEEKEND);
        return weekendScheduleDays.stream()
                .map(day -> DayOfWeek.valueOf(day.getDayOfWeekEntity().getCode()))
                .collect(Collectors.toSet());
    }

    /**
     * Main and Specials are resolved independently — the room's own row(s) if it has any, else falls back to
     * the category's, exactly mirroring ResortRoomPriceServiceImpl#getAllGroupedByCurrency's mainInherited/
     * specialsInherited logic (see that method's body for the precedent this is copied from). Empty means no
     * Main price is resolvable at all (room has none, category has none either) — the one condition that
     * makes a room unpriceable for this currency; every throwing/non-throwing caller funnels through here.
     */
    private Optional<PricingInputs> tryResolvePricingInputs(ResortRoomEntity resortRoomEntity, Long resortRoomCategoryId, Long currencyId) {
        Long resortRoomId = resortRoomEntity.getId();
        Optional<ResortRoomMainPriceEntity> ownMain = resortRoomPriceService.getMainEntityByCurrency(resortRoomId, currencyId);
        List<ResortRoomSpecialPriceEntity> ownSpecials = resortRoomPriceService.getSpecialEntitiesByCurrency(resortRoomId, currencyId);

        PriceUnitEntity priceUnitEntity;
        BigDecimal weekdayPrice;
        BigDecimal weekendPrice;

        if (ownMain.isPresent()) {
            priceUnitEntity = ownMain.get().getPriceUnitEntity();
            weekdayPrice = ownMain.get().getWeekdayPrice();
            weekendPrice = ownMain.get().getWeekendPrice();
        } else {
            Optional<ResortRoomCategoryMainPriceEntity> categoryMain = resortRoomCategoryPriceService
                    .getMainEntityByCurrency(resortRoomCategoryId, currencyId);
            if (categoryMain.isEmpty()) {
                return Optional.empty();
            }
            priceUnitEntity = categoryMain.get().getPriceUnitEntity();
            weekdayPrice = categoryMain.get().getWeekdayPrice();
            weekendPrice = categoryMain.get().getWeekendPrice();
        }

        List<PricingCalculator.SpecialPriceWindow> specialWindows = ownSpecials.isEmpty()
                ? resortRoomCategoryPriceService.getSpecialEntitiesByCurrency(resortRoomCategoryId, currencyId).stream()
                        .map(special -> new PricingCalculator.SpecialPriceWindow(
                                special.getValidFrom(), special.getValidTo(),
                                special.getWeekdayPrice(), special.getWeekendPrice(), special.getPriority()))
                        .toList()
                : ownSpecials.stream()
                        .map(special -> new PricingCalculator.SpecialPriceWindow(
                                special.getValidFrom(), special.getValidTo(),
                                special.getWeekdayPrice(), special.getWeekendPrice(), special.getPriority()))
                        .toList();

        return Optional.of(new PricingInputs(priceUnitEntity, weekdayPrice, weekendPrice, specialWindows));
    }

    private record PricingInputs(PriceUnitEntity priceUnitEntity, BigDecimal weekdayPrice, BigDecimal weekendPrice,
                                 List<PricingCalculator.SpecialPriceWindow> specialWindows) {
    }
}
