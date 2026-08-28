package com.example.resortbackendapplication1.resort.facility.serviceImpl;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.utils.Pagination;
import com.example.resortbackendapplication1.dayofweek.model.dto.DayOfWeekDto;
import com.example.resortbackendapplication1.dayofweek.model.entity.DayOfWeekEntity;
import com.example.resortbackendapplication1.dayofweek.model.mapper.DayOfWeekMapper;
import com.example.resortbackendapplication1.resort.facility.dto.request.resortfacilityoperatinghours.ResortFacilityOperatingHoursDayScheduleRequest;
import com.example.resortbackendapplication1.resort.facility.dto.request.resortfacilityoperatinghours.ResortFacilityOperatingHoursWindowRequest;
import com.example.resortbackendapplication1.resort.facility.dto.request.resortfacilityoperatinghours.SetResortFacilityOperatingHoursScheduleRequest;
import com.example.resortbackendapplication1.resort.facility.dto.response.resortfacilityoperatinghours.ResortFacilityOperatingHoursScheduleResponse;
import com.example.resortbackendapplication1.resort.facility.model.dto.ResortFacilityDto;
import com.example.resortbackendapplication1.resort.facility.model.dto.ResortFacilityOperatingHoursDto;
import com.example.resortbackendapplication1.resort.facility.model.entity.ResortFacilityEntity;
import com.example.resortbackendapplication1.resort.facility.model.entity.ResortFacilityOperatingHoursEntity;
import com.example.resortbackendapplication1.resort.facility.model.mapper.ResortFacilityMapper;
import com.example.resortbackendapplication1.resort.facility.model.mapper.ResortFacilityOperatingHoursMapper;
import com.example.resortbackendapplication1.resort.facility.repository.ResortFacilityOperatingHoursRepository;
import com.example.resortbackendapplication1.resort.facility.service.ResortFacilityOperatingHoursService;
import com.example.resortbackendapplication1.resort.facility.validation.ResortFacilityOperatingHoursScheduleValidator;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ResortFacilityOperatingHoursServiceImpl implements ResortFacilityOperatingHoursService {

    private final ResortFacilityOperatingHoursRepository resortFacilityOperatingHoursRepository;

    public ResortFacilityOperatingHoursServiceImpl(
            ResortFacilityOperatingHoursRepository resortFacilityOperatingHoursRepository) {
        this.resortFacilityOperatingHoursRepository = resortFacilityOperatingHoursRepository;
    }

    @Override
    public PaginatedResponse<ResortFacilityOperatingHoursDto> getAll(Long facilityId, PaginatedRequest request) {
        Pageable pageable = request.toPageable(Set.of());
        Page<@NonNull ResortFacilityOperatingHoursDto> page = resortFacilityOperatingHoursRepository
                .findByResortFacilityEntity_IdAndIsActiveAndIsDeleted(facilityId, true, false, pageable)
                .map(entity -> ResortFacilityOperatingHoursMapper.toDto(entity)
                        .resortFacility(ResortFacilityMapper.toDto(entity.getResortFacilityEntity()).build())
                        .dayOfWeek(DayOfWeekMapper.toDto(entity.getDayOfWeekEntity()).build())
                        .build());
        return Pagination.buildPaginatedResponse(page);
    }

    @Transactional
    @Override
    public ResortFacilityOperatingHoursScheduleResponse setWeeklySchedule(
            ResortFacilityEntity resortFacilityEntity,
            SetResortFacilityOperatingHoursScheduleRequest request,
            List<DayOfWeekEntity> allDaysOfWeek) {
        Map<Long, DayOfWeekEntity> daysOfWeekById = allDaysOfWeek.stream()
                .collect(Collectors.toMap(DayOfWeekEntity::getId, dayOfWeekEntity -> dayOfWeekEntity));
        List<ResortFacilityOperatingHoursDayScheduleRequest> days = request.getDays();

        ResortFacilityOperatingHoursScheduleValidator.validateWeekCompleteness(days, daysOfWeekById.keySet());
        days.forEach(ResortFacilityOperatingHoursScheduleValidator::validateDayShape);
        days.forEach(day -> ResortFacilityOperatingHoursScheduleValidator.validateWindowsDoNotOverlap(day.getWindows()));
        ResortFacilityOperatingHoursScheduleValidator.validateSpilloverAcrossWeek(days, allDaysOfWeek);

        List<ResortFacilityOperatingHoursEntity> existing = resortFacilityOperatingHoursRepository
                .findAllByResortFacilityEntity_IdAndIsActiveAndIsDeleted(resortFacilityEntity.getId(), true, false);
        existing.forEach(row -> {
            row.setIsDeleted(true);
            row.setIsActive(false);
        });
        resortFacilityOperatingHoursRepository.saveAll(existing);

        List<ResortFacilityOperatingHoursEntity> created = new ArrayList<>();
        for (ResortFacilityOperatingHoursDayScheduleRequest day : days) {
            DayOfWeekEntity dayOfWeekEntity = daysOfWeekById.get(day.getDayOfWeekId());
            if (Boolean.TRUE.equals(day.getIsClosed()) || Boolean.TRUE.equals(day.getIsTwentyFourHours())) {
                created.add(buildRow(resortFacilityEntity, dayOfWeekEntity,
                        day.getIsClosed(), day.getIsTwentyFourHours(), null, null));
            } else {
                for (ResortFacilityOperatingHoursWindowRequest window : day.getWindows()) {
                    created.add(buildRow(resortFacilityEntity, dayOfWeekEntity,
                            false, false, window.getOpensAt(), window.getClosesAt()));
                }
            }
        }
        resortFacilityOperatingHoursRepository.saveAll(created);
        log.info("ResortFacilityOperatingHours weekly schedule set for facility id: {} ({} rows, replacing {} old rows)",
                resortFacilityEntity.getId(), created.size(), existing.size());

        List<ResortFacilityOperatingHoursDto> dtos = created.stream()
                .map(entity -> ResortFacilityOperatingHoursMapper.toDto(entity)
                        .dayOfWeek(DayOfWeekMapper.toDto(entity.getDayOfWeekEntity()).build())
                        .build())
                .toList();
        return new ResortFacilityOperatingHoursScheduleResponse(dtos);
    }

    private ResortFacilityOperatingHoursEntity buildRow(ResortFacilityEntity resortFacilityEntity,
                                                          DayOfWeekEntity dayOfWeekEntity,
                                                          Boolean isClosed, Boolean isTwentyFourHours,
                                                          LocalTime opensAt, LocalTime closesAt) {
        ResortFacilityOperatingHoursEntity entity = ResortFacilityOperatingHoursMapper.create(
                isClosed, isTwentyFourHours, opensAt, closesAt);
        resortFacilityEntity.addResortFacilityOperatingHoursEntity(entity);
        dayOfWeekEntity.addResortFacilityOperatingHoursEntity(entity);
        return entity;
    }
}
