package com.example.resortbackendapplication1.resort.validation;

import com.example.resortbackendapplication1.dayofweek.model.entity.DayOfWeekEntity;
import com.example.resortbackendapplication1.resort.dto.request.resortfacilityoperatinghours.ResortFacilityOperatingHoursDayScheduleRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortfacilityoperatinghours.ResortFacilityOperatingHoursWindowRequest;
import jakarta.persistence.EntityNotFoundException;
import lombok.experimental.UtilityClass;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Same-day/cross-day overlap math and whole-week schedule validation, shared between
 * {@code ResortFacilityOperatingHoursServiceImpl} (per-row {@code create}/{@code update} and
 * {@code setWeeklySchedule}) and {@code ResortFacilityServiceImpl} (the operating-hours schedule embedded in
 * {@code Create Resort Facility}). See {@code docs/resort-facility-operating-hours-design.md} §5-6.
 */
@UtilityClass
public class ResortFacilityOperatingHoursScheduleValidator {

    public boolean windowsOverlap(LocalTime aStart, LocalTime aEnd, LocalTime bStart, LocalTime bEnd) {
        for (int[] a : toIntervals(aStart, aEnd)) {
            for (int[] b : toIntervals(bStart, bEnd)) {
                if (Math.max(a[0], b[0]) < Math.min(a[1], b[1])) {
                    return true;
                }
            }
        }
        return false;
    }

    /** Splits an overnight window (end <= start) into its two wrapped sub-intervals on a same-day/next-day timeline. */
    public List<int[]> toIntervals(LocalTime start, LocalTime end) {
        int startMinutes = start.toSecondOfDay() / 60;
        int endMinutes = end.toSecondOfDay() / 60;
        if (endMinutes > startMinutes) {
            return List.of(new int[]{startMinutes, endMinutes});
        }
        return List.of(new int[]{startMinutes, 1440}, new int[]{0, endMinutes});
    }

    public boolean hasSpillover(LocalTime opensAt, LocalTime closesAt) {
        return closesAt.toSecondOfDay() <= opensAt.toSecondOfDay();
    }

    /**
     * Checks only the wrapped tail of {@code prevOpensAt}-{@code prevClosesAt} (the portion that lands on the
     * *next* calendar day, i.e. minutes [0, prevClosesAt)) against a window on that next day. The previous
     * window's own same-day portion is irrelevant here — it never coincides with the next day's timeline.
     */
    public boolean spilloverOverlapsWindow(LocalTime prevOpensAt, LocalTime prevClosesAt,
                                            LocalTime nextOpensAt, LocalTime nextClosesAt) {
        int spilloverEnd = prevClosesAt.toSecondOfDay() / 60;
        for (int[] b : toIntervals(nextOpensAt, nextClosesAt)) {
            if (Math.max(0, b[0]) < Math.min(spilloverEnd, b[1])) {
                return true;
            }
        }
        return false;
    }

    public void validateWeekCompleteness(List<ResortFacilityOperatingHoursDayScheduleRequest> days,
                                          Set<Long> activeDayOfWeekIds) {
        Set<Long> requestedIds = days.stream()
                .map(ResortFacilityOperatingHoursDayScheduleRequest::getDayOfWeekId)
                .collect(Collectors.toSet());
        if (requestedIds.size() != days.size()) {
            throw new IllegalArgumentException("Schedule request contains duplicate day_of_week_id values");
        }
        Set<Long> unknownIds = requestedIds.stream()
                .filter(id -> !activeDayOfWeekIds.contains(id))
                .collect(Collectors.toSet());
        if (!unknownIds.isEmpty()) {
            throw new EntityNotFoundException("DayOfWeek not found with ids: " + unknownIds);
        }
        Set<Long> missingIds = activeDayOfWeekIds.stream()
                .filter(id -> !requestedIds.contains(id))
                .collect(Collectors.toSet());
        if (!missingIds.isEmpty()) {
            throw new IllegalArgumentException(
                    "Schedule request must cover every day of week; missing day_of_week ids: " + missingIds);
        }
    }

    public void validateDayShape(ResortFacilityOperatingHoursDayScheduleRequest day) {
        boolean isClosed = Boolean.TRUE.equals(day.getIsClosed());
        boolean isTwentyFourHours = Boolean.TRUE.equals(day.getIsTwentyFourHours());
        if (isClosed && isTwentyFourHours) {
            throw new IllegalArgumentException(
                    "is_closed and is_twenty_four_hours cannot both be true (day_of_week_id: " + day.getDayOfWeekId() + ")");
        }
        if (isClosed || isTwentyFourHours) {
            if (!day.getWindows().isEmpty()) {
                throw new IllegalArgumentException(
                        "windows must be empty when is_closed or is_twenty_four_hours is true (day_of_week_id: "
                                + day.getDayOfWeekId() + ")");
            }
        } else if (day.getWindows().isEmpty()) {
            throw new IllegalArgumentException(
                    "At least one window is required when is_closed and is_twenty_four_hours are both false (day_of_week_id: "
                            + day.getDayOfWeekId() + ")");
        }
    }

    public void validateWindowsDoNotOverlap(List<ResortFacilityOperatingHoursWindowRequest> windows) {
        for (int i = 0; i < windows.size(); i++) {
            for (int j = i + 1; j < windows.size(); j++) {
                ResortFacilityOperatingHoursWindowRequest a = windows.get(i);
                ResortFacilityOperatingHoursWindowRequest b = windows.get(j);
                if (windowsOverlap(a.getOpensAt(), a.getClosesAt(), b.getOpensAt(), b.getClosesAt())) {
                    throw new IllegalStateException("Windows " + a.getOpensAt() + "-" + a.getClosesAt()
                            + " and " + b.getOpensAt() + "-" + b.getClosesAt() + " overlap");
                }
            }
        }
    }

    /** Walks the week in {@code allDaysOfWeek} order (wrapping last -> first) checking each day's spillover into the next. */
    public void validateSpilloverAcrossWeek(List<ResortFacilityOperatingHoursDayScheduleRequest> days,
                                             List<DayOfWeekEntity> allDaysOfWeek) {
        Map<Long, ResortFacilityOperatingHoursDayScheduleRequest> byDayId = days.stream()
                .collect(Collectors.toMap(ResortFacilityOperatingHoursDayScheduleRequest::getDayOfWeekId, d -> d));
        int size = allDaysOfWeek.size();
        for (int i = 0; i < size; i++) {
            ResortFacilityOperatingHoursDayScheduleRequest current = byDayId.get(allDaysOfWeek.get(i).getId());
            ResortFacilityOperatingHoursDayScheduleRequest next = byDayId.get(allDaysOfWeek.get((i + 1) % size).getId());
            if (Boolean.TRUE.equals(current.getIsClosed()) || Boolean.TRUE.equals(current.getIsTwentyFourHours())) {
                continue;
            }
            for (ResortFacilityOperatingHoursWindowRequest window : current.getWindows()) {
                if (!hasSpillover(window.getOpensAt(), window.getClosesAt())) {
                    continue;
                }
                if (Boolean.TRUE.equals(next.getIsClosed())) {
                    throw new IllegalStateException("Window " + window.getOpensAt() + "-" + window.getClosesAt()
                            + " on day_of_week_id " + current.getDayOfWeekId() + " spills into day_of_week_id "
                            + next.getDayOfWeekId() + ", which is marked is_closed");
                }
                if (Boolean.TRUE.equals(next.getIsTwentyFourHours())) {
                    continue;
                }
                for (ResortFacilityOperatingHoursWindowRequest nextWindow : next.getWindows()) {
                    if (spilloverOverlapsWindow(window.getOpensAt(), window.getClosesAt(),
                            nextWindow.getOpensAt(), nextWindow.getClosesAt())) {
                        throw new IllegalStateException("Window " + window.getOpensAt() + "-" + window.getClosesAt()
                                + " on day_of_week_id " + current.getDayOfWeekId() + " spills into day_of_week_id "
                                + next.getDayOfWeekId() + " and overlaps its window " + nextWindow.getOpensAt()
                                + "-" + nextWindow.getClosesAt());
                    }
                }
            }
        }
    }
}
