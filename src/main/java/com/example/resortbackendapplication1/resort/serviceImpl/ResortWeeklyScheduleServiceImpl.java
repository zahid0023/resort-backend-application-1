package com.example.resortbackendapplication1.resort.serviceImpl;

import com.example.resortbackendapplication1.dayofweek.model.entity.DayOfWeekEntity;
import com.example.resortbackendapplication1.dayofweek.model.mapper.DayOfWeekMapper;
import com.example.resortbackendapplication1.price.model.entity.PriceTypeEntity;
import com.example.resortbackendapplication1.resort.dto.response.resortweeklyschedule.ResortWeeklyScheduleResponse;
import com.example.resortbackendapplication1.resort.model.dto.ResortWeeklyScheduleDayDto;
import com.example.resortbackendapplication1.resort.model.entity.ResortEntity;
import com.example.resortbackendapplication1.resort.model.entity.ResortWeeklyScheduleDayEntity;
import com.example.resortbackendapplication1.resort.model.mapper.ResortWeeklyScheduleDayMapper;
import com.example.resortbackendapplication1.resort.repository.ResortWeeklyScheduleDayRepository;
import com.example.resortbackendapplication1.resort.service.ResortWeeklyScheduleService;
import com.example.resortbackendapplication1.resort.validation.ResortWeeklyScheduleValidator;
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
    public List<ResortWeeklyScheduleDayEntity> getEntitiesByPriceType(ResortEntity resortEntity, String priceTypeCode) {
        return resortWeeklyScheduleDayRepository
                .findAllByResortEntity_IdAndPriceTypeEntity_CodeAndIsActiveAndIsDeleted(
                        resortEntity.getId(), priceTypeCode, true, false);
    }

    @Transactional
    @Override
    public ResortWeeklyScheduleResponse updateWeeklySchedule(ResortEntity resortEntity,
                                                              PriceTypeEntity weekdayPriceTypeEntity,
                                                              PriceTypeEntity weekendPriceTypeEntity,
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
                created.add(buildRow(resortEntity, weekdayPriceTypeEntity, dayOfWeekEntity)));
        weekendDayOfWeekEntities.forEach(dayOfWeekEntity ->
                created.add(buildRow(resortEntity, weekendPriceTypeEntity, dayOfWeekEntity)));
        resortWeeklyScheduleDayRepository.saveAll(created);

        log.info("ResortWeeklySchedule set for resort id: {} ({} rows, replacing {} old rows)",
                resortEntity.getId(), created.size(), existing.size());

        return buildResponse(created);
    }

    private ResortWeeklyScheduleDayEntity buildRow(ResortEntity resortEntity, PriceTypeEntity priceTypeEntity,
                                                     DayOfWeekEntity dayOfWeekEntity) {
        ResortWeeklyScheduleDayEntity entity = ResortWeeklyScheduleDayMapper.create(priceTypeEntity);
        resortEntity.addResortWeeklyScheduleDayEntity(entity);
        dayOfWeekEntity.addResortWeeklyScheduleDayEntity(entity);
        return entity;
    }

    private ResortWeeklyScheduleResponse buildResponse(List<ResortWeeklyScheduleDayEntity> entities) {
        List<ResortWeeklyScheduleDayDto> weekday = entities.stream()
                .filter(entity -> "WKD".equals(entity.getPriceTypeEntity().getCode()))
                .map(this::toDto)
                .toList();
        List<ResortWeeklyScheduleDayDto> weekend = entities.stream()
                .filter(entity -> "WKE".equals(entity.getPriceTypeEntity().getCode()))
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
