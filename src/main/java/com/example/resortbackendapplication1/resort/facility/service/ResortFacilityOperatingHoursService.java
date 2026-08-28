package com.example.resortbackendapplication1.resort.facility.service;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.dayofweek.model.entity.DayOfWeekEntity;
import com.example.resortbackendapplication1.resort.facility.dto.request.resortfacilityoperatinghours.SetResortFacilityOperatingHoursScheduleRequest;
import com.example.resortbackendapplication1.resort.facility.dto.response.resortfacilityoperatinghours.ResortFacilityOperatingHoursScheduleResponse;
import com.example.resortbackendapplication1.resort.facility.model.dto.ResortFacilityOperatingHoursDto;
import com.example.resortbackendapplication1.resort.facility.model.entity.ResortFacilityEntity;

import java.util.List;

/**
 * List access plus the atomic whole-week write path only — there is no per-row create/update/delete/get-by-id.
 * A facility's schedule is always written as a complete week (see {@link #setWeeklySchedule}), either at
 * creation ({@code ResortFacilityService#create}, which builds rows directly) or afterward via this service.
 */
public interface ResortFacilityOperatingHoursService {

    PaginatedResponse<ResortFacilityOperatingHoursDto> getAll(Long facilityId, PaginatedRequest request);

    /**
     * Atomically replaces every active operating-hours row for the facility. {@code request} must cover every
     * entry in {@code allDaysOfWeek} exactly once — see {@link SetResortFacilityOperatingHoursScheduleRequest}.
     */
    ResortFacilityOperatingHoursScheduleResponse setWeeklySchedule(ResortFacilityEntity resortFacilityEntity,
                                                                    SetResortFacilityOperatingHoursScheduleRequest request,
                                                                    List<DayOfWeekEntity> allDaysOfWeek);
}
