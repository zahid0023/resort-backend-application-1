package com.example.resortbackendapplication1.resort.core.serviceImpl;

import com.example.resortbackendapplication1.dayofweek.model.entity.DayOfWeekEntity;
import com.example.resortbackendapplication1.dayofweek.model.mapper.DayOfWeekMapper;
import com.example.resortbackendapplication1.resort.core.dto.response.resortweeklyschedule.ResortWeeklyScheduleResponse;
import com.example.resortbackendapplication1.resort.core.model.dto.ResortWeeklyScheduleDayDto;
import com.example.resortbackendapplication1.resort.core.model.entity.ResortEntity;
import com.example.resortbackendapplication1.resort.core.model.entity.ResortWeeklyScheduleDayEntity;
import com.example.resortbackendapplication1.resort.core.model.enums.DayType;
import com.example.resortbackendapplication1.resort.core.model.mapper.ResortWeeklyScheduleDayMapper;
import com.example.resortbackendapplication1.resort.core.repository.ResortWeeklyScheduleDayRepository;
import com.example.resortbackendapplication1.resort.core.service.ResortWeeklyScheduleService;
import com.example.resortbackendapplication1.resort.core.validation.ResortWeeklyScheduleValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ResortWeeklyScheduleServiceImpl implements ResortWeeklyScheduleService {

    private final ResortWeeklyScheduleDayRepository resortWeeklyScheduleDayRepository;

    public ResortWeeklyScheduleServiceImpl(ResortWeeklyScheduleDayRepository resortWeeklyScheduleDayRepository) {
        this.resortWeeklyScheduleDayRepository = resortWeeklyScheduleDayRepository;
    }

    @Override
    public ResortWeeklyScheduleResponse getWeeklySchedule(ResortEntity resortEntity) {
        List<ResortWeeklyScheduleDayEntity> entities = resortWeeklyScheduleDayRepository
                .findAllByResortEntity_IdAndIsActiveAndIsDeleted(resortEntity.getId(), true, false);
        return buildResponse(entities);
    }

    @Override
    public List<ResortWeeklyScheduleDayEntity> getEntitiesByDayType(ResortEntity resortEntity, DayType dayType) {
        return resortWeeklyScheduleDayRepository
                .findAllByResortEntity_IdAndDayTypeAndIsActiveAndIsDeleted(
                        resortEntity.getId(), dayType, true, false);
    }

    @Transactional
    @Override
    public ResortWeeklyScheduleResponse updateWeeklySchedule(ResortEntity resortEntity,
                                                              List<DayOfWeekEntity> weekdayDayOfWeekEntities,
                                                              List<DayOfWeekEntity> weekendDayOfWeekEntities) {
        ResortWeeklyScheduleValidator.validateNoDuplicateDays(weekdayDayOfWeekEntities, "weekday_day_of_week_ids");
        ResortWeeklyScheduleValidator.validateNoDuplicateDays(weekendDayOfWeekEntities, "weekend_day_of_week_ids");
        ResortWeeklyScheduleValidator.validateNoOverlappingDays(weekdayDayOfWeekEntities, weekendDayOfWeekEntities);

        List<ResortWeeklyScheduleDayEntity> existing = resortWeeklyScheduleDayRepository
                .findAllByResortEntity_IdAndIsActiveAndIsDeleted(resortEntity.getId(), true, false);
        existing.forEach(row -> {
            row.setIsDeleted(true);
            row.setIsActive(false);
        });
        resortWeeklyScheduleDayRepository.saveAll(existing);

        List<ResortWeeklyScheduleDayEntity> created = new ArrayList<>();
        weekdayDayOfWeekEntities.forEach(dayOfWeekEntity ->
                created.add(buildRow(resortEntity, DayType.WEEKDAY, dayOfWeekEntity)));
        weekendDayOfWeekEntities.forEach(dayOfWeekEntity ->
                created.add(buildRow(resortEntity, DayType.WEEKEND, dayOfWeekEntity)));
        resortWeeklyScheduleDayRepository.saveAll(created);

        log.info("ResortWeeklySchedule set for resort id: {} ({} rows, replacing {} old rows)",
                resortEntity.getId(), created.size(), existing.size());

        return buildResponse(created);
    }

    private ResortWeeklyScheduleDayEntity buildRow(ResortEntity resortEntity, DayType dayType,
                                                     DayOfWeekEntity dayOfWeekEntity) {
        ResortWeeklyScheduleDayEntity entity = ResortWeeklyScheduleDayMapper.create(dayType);
        resortEntity.addResortWeeklyScheduleDayEntity(entity);
        dayOfWeekEntity.addResortWeeklyScheduleDayEntity(entity);
        return entity;
    }

    private ResortWeeklyScheduleResponse buildResponse(List<ResortWeeklyScheduleDayEntity> entities) {
        List<ResortWeeklyScheduleDayDto> weekday = entities.stream()
                .filter(entity -> entity.getDayType() == DayType.WEEKDAY)
                .map(this::toDto)
                .toList();
        List<ResortWeeklyScheduleDayDto> weekend = entities.stream()
                .filter(entity -> entity.getDayType() == DayType.WEEKEND)
                .map(this::toDto)
                .toList();
        return new ResortWeeklyScheduleResponse(weekday, weekend);
    }

    private ResortWeeklyScheduleDayDto toDto(ResortWeeklyScheduleDayEntity entity) {
        return ResortWeeklyScheduleDayMapper.toDto(entity)
                .dayOfWeek(DayOfWeekMapper.toDto(entity.getDayOfWeekEntity()).build())
                .build();
    }
}
