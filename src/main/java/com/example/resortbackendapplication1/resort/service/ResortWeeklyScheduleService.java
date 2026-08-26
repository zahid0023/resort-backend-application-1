package com.example.resortbackendapplication1.resort.service;

import com.example.resortbackendapplication1.dayofweek.model.entity.DayOfWeekEntity;
import com.example.resortbackendapplication1.price.model.entity.PriceTypeEntity;
import com.example.resortbackendapplication1.resort.dto.response.resortweeklyschedule.ResortWeeklyScheduleResponse;
import com.example.resortbackendapplication1.resort.model.entity.ResortEntity;
import com.example.resortbackendapplication1.resort.model.entity.ResortWeeklyScheduleDayEntity;

import java.util.List;

/**
 * Which days count as WEEKDAY/WEEKEND for a resort — shared by every room category at that resort, not scoped
 * per currency (see {@code resort_weekly_schedule_days} in the V35 migration). List/read plus the atomic
 * whole-schedule write path only — there is no per-row create/update/delete. A resort's schedule must exist
 * before any of its room categories can get an active WEEKDAY/WEEKEND price (DB-trigger-enforced).
 */
public interface ResortWeeklyScheduleService {

    ResortWeeklyScheduleResponse getWeeklySchedule(ResortEntity resortEntity);

    /**
     * Every active weekly-schedule row for one price type (`WKD` or `WKE`) — called from
     * {@code ResortRoomCategoryPriceController} to embed a room category price row's {@code days} field, since
     * the schedule is resort-level, not owned by the price row itself, and a ServiceImpl must never call
     * another domain's Service to fetch it directly.
     */
    List<ResortWeeklyScheduleDayEntity> getEntitiesByPriceType(ResortEntity resortEntity, String priceTypeCode);

    /**
     * Atomically replaces every active weekly-schedule row for the resort — the existing active rows are
     * soft-deleted and a fresh set built from {@code weekdayDayOfWeekEntities}/{@code weekendDayOfWeekEntities}
     * is inserted, in one transaction, mirroring {@code ResortFacilityOperatingHoursServiceImpl.setWeeklySchedule}
     * (that sibling feature keeps its original method name; this one is called {@code updateWeeklySchedule} since
     * it is reached via {@code PUT} and only ever runs after the resort's initial schedule already exists).
     */
    ResortWeeklyScheduleResponse updateWeeklySchedule(ResortEntity resortEntity,
                                                      PriceTypeEntity weekdayPriceTypeEntity,
                                                      PriceTypeEntity weekendPriceTypeEntity,
                                                      List<DayOfWeekEntity> weekdayDayOfWeekEntities,
                                                      List<DayOfWeekEntity> weekendDayOfWeekEntities);
}
