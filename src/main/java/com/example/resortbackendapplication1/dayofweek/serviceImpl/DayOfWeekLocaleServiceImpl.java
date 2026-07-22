package com.example.resortbackendapplication1.dayofweek.serviceImpl;

import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.dayofweek.dto.request.dayofweek.dayofweeklocale.CreateDayOfWeekLocaleRequest;
import com.example.resortbackendapplication1.dayofweek.dto.request.dayofweek.dayofweeklocale.UpdateDayOfWeekLocaleRequest;
import com.example.resortbackendapplication1.dayofweek.model.entity.DayOfWeekEntity;
import com.example.resortbackendapplication1.dayofweek.model.entity.DayOfWeekLocaleEntity;
import com.example.resortbackendapplication1.dayofweek.model.mapper.DayOfWeekLocaleMapper;
import com.example.resortbackendapplication1.dayofweek.repository.DayOfWeekLocaleRepository;
import com.example.resortbackendapplication1.dayofweek.service.DayOfWeekLocaleService;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class DayOfWeekLocaleServiceImpl implements DayOfWeekLocaleService {

    private final DayOfWeekLocaleRepository dayOfWeekLocaleRepository;

    public DayOfWeekLocaleServiceImpl(DayOfWeekLocaleRepository dayOfWeekLocaleRepository) {
        this.dayOfWeekLocaleRepository = dayOfWeekLocaleRepository;
    }

    @Transactional
    @Override
    public SuccessResponse create(DayOfWeekEntity dayOfWeekEntity,
                                  LocaleEntity localeEntity,
                                  CreateDayOfWeekLocaleRequest request) {
        DayOfWeekLocaleEntity entity = DayOfWeekLocaleMapper.create(request, dayOfWeekEntity, localeEntity);
        dayOfWeekLocaleRepository.save(entity);
        log.info("DayOfWeekLocale created with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public DayOfWeekLocaleEntity getEntityById(Long dayOfWeekId, Long id) {
        return dayOfWeekLocaleRepository
                .findByDayOfWeekEntity_IdAndIdAndIsActiveAndIsDeleted(dayOfWeekId, id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("DayOfWeekLocale not found with id: " + id));
    }

    @Transactional
    @Override
    public SuccessResponse update(DayOfWeekLocaleEntity entity, UpdateDayOfWeekLocaleRequest request) {
        DayOfWeekLocaleMapper.update(entity, request);
        dayOfWeekLocaleRepository.save(entity);
        log.info("DayOfWeekLocale updated with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse delete(DayOfWeekLocaleEntity entity) {
        entity.setIsDeleted(true);
        entity.setIsActive(false);
        dayOfWeekLocaleRepository.save(entity);
        log.info("DayOfWeekLocale soft-deleted with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }
}
