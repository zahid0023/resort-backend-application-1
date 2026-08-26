package com.example.resortbackendapplication1.resort.validation;

import com.example.resortbackendapplication1.dayofweek.model.entity.DayOfWeekEntity;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Shared by {@code ResortWeeklyScheduleServiceImpl.updateWeeklySchedule} and any other caller building a resort's
 * weekly schedule from raw day-of-week ids — kept as a stateless utility (not a Service) so it can be called
 * directly by any ServiceImpl that needs it without one ServiceImpl having to call another.
 */
@UtilityClass
public class ResortWeeklyScheduleValidator {

    /**
     * Without this, a duplicate id in a day list (e.g. {@code [3, 3, 5]}) would only be caught by a database
     * error once two identical rows are inserted, surfacing as an unfriendly {@code 409
     * DATA_INTEGRITY_VIOLATION} instead of a clean, up-front error.
     */
    public void validateNoDuplicateDays(List<DayOfWeekEntity> dayOfWeekEntities, String fieldName) {
        Set<Long> distinctIds = dayOfWeekEntities.stream().map(DayOfWeekEntity::getId).collect(Collectors.toSet());
        if (distinctIds.size() != dayOfWeekEntities.size()) {
            throw new IllegalStateException("Duplicate day of week ids are not allowed in " + fieldName);
        }
    }

    /**
     * Without this, the same day of week could be assigned to both {@code weekday_day_of_week_ids} and
     * {@code weekend_day_of_week_ids} in the same request — nothing else stops that day from ending up with
     * two simultaneously "active" rates for every room category at the resort.
     */
    public void validateNoOverlappingDays(List<DayOfWeekEntity> weekdayDayOfWeekEntities,
                                           List<DayOfWeekEntity> weekendDayOfWeekEntities) {
        Set<Long> weekdayIds = weekdayDayOfWeekEntities.stream().map(DayOfWeekEntity::getId).collect(Collectors.toSet());
        Set<Long> overlappingIds = weekendDayOfWeekEntities.stream()
                .map(DayOfWeekEntity::getId)
                .filter(weekdayIds::contains)
                .collect(Collectors.toSet());
        if (!overlappingIds.isEmpty()) {
            throw new IllegalStateException(
                    "The same day of week cannot be assigned to both weekday_day_of_week_ids and weekend_day_of_week_ids: "
                            + overlappingIds);
        }
    }
}
