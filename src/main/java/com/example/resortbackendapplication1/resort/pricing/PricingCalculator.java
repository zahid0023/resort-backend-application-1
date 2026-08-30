package com.example.resortbackendapplication1.resort.pricing;

import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

/**
 * Pure computation only — no repository/Service access, no Spring dependency, so it's usable and unit-testable
 * from any module without pulling in a domain it doesn't otherwise need. Every input (weekday/weekend rates,
 * special windows, which days count as "weekend") is resolved by the caller — see {@link RoomPricingResolver}
 * for the one that does that resolution against the Room/RoomCategory/Resort domains.
 *
 * <p>Precedence per night, per docs/resort-room-prices-api.md: Special (highest {@code priority} among
 * windows covering that night) &gt; Weekday/Weekend from the resolved Main row.
 */
@UtilityClass
public class PricingCalculator {

    public record SpecialPriceWindow(LocalDate validFrom, LocalDate validTo,
                                     BigDecimal weekdayPrice, BigDecimal weekendPrice, Integer priority) {
    }

    public enum RateType {
        WEEKDAY, WEEKEND, SPECIAL
    }

    public record NightlyRate(LocalDate date, BigDecimal price, RateType rateType) {
    }

    /** {@code checkOut} is exclusive, matching the stay's half-open [checkIn, checkOut) range. */
    public BigDecimal calculate(LocalDate checkIn, LocalDate checkOut, Set<DayOfWeek> weekendDays,
                                BigDecimal mainWeekdayPrice, BigDecimal mainWeekendPrice,
                                List<SpecialPriceWindow> specialWindows) {
        return calculateNightly(checkIn, checkOut, weekendDays, mainWeekdayPrice, mainWeekendPrice, specialWindows)
                .stream()
                .map(NightlyRate::price)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /** Same rule as {@link #calculate}, broken out night by night instead of summed. */
    public List<NightlyRate> calculateNightly(LocalDate checkIn, LocalDate checkOut, Set<DayOfWeek> weekendDays,
                                              BigDecimal mainWeekdayPrice, BigDecimal mainWeekendPrice,
                                              List<SpecialPriceWindow> specialWindows) {
        List<NightlyRate> nights = new ArrayList<>();
        for (LocalDate night = checkIn; night.isBefore(checkOut); night = night.plusDays(1)) {
            nights.add(rateForNight(night, weekendDays, mainWeekdayPrice, mainWeekendPrice, specialWindows));
        }
        return nights;
    }

    private NightlyRate rateForNight(LocalDate night, Set<DayOfWeek> weekendDays,
                                     BigDecimal mainWeekdayPrice, BigDecimal mainWeekendPrice,
                                     List<SpecialPriceWindow> specialWindows) {
        boolean isWeekend = weekendDays.contains(night.getDayOfWeek());
        SpecialPriceWindow applicable = specialWindows.stream()
                .filter(window -> !night.isBefore(window.validFrom()) && !night.isAfter(window.validTo()))
                .max(Comparator.comparingInt(SpecialPriceWindow::priority))
                .orElse(null);
        if (applicable != null) {
            BigDecimal price = isWeekend ? applicable.weekendPrice() : applicable.weekdayPrice();
            return new NightlyRate(night, price, RateType.SPECIAL);
        }
        BigDecimal price = isWeekend ? mainWeekendPrice : mainWeekdayPrice;
        return new NightlyRate(night, price, isWeekend ? RateType.WEEKEND : RateType.WEEKDAY);
    }
}
