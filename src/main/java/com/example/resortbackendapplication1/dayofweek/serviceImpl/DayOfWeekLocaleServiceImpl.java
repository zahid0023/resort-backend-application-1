package com.example.resortbackendapplication1.dayofweek.serviceImpl;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.commons.utils.Pagination;
import com.example.resortbackendapplication1.dayofweek.dto.request.dayofweek.locale.CreateDayOfWeekLocaleRequest;
import com.example.resortbackendapplication1.dayofweek.dto.request.dayofweek.locale.UpdateDayOfWeekLocaleRequest;
import com.example.resortbackendapplication1.dayofweek.model.dto.DayOfWeekLocaleDto;
import com.example.resortbackendapplication1.dayofweek.model.entity.DayOfWeekEntity;
import com.example.resortbackendapplication1.dayofweek.model.entity.DayOfWeekLocaleEntity;
import com.example.resortbackendapplication1.dayofweek.model.mapper.DayOfWeekLocaleMapper;
import com.example.resortbackendapplication1.dayofweek.repository.DayOfWeekLocaleRepository;
import com.example.resortbackendapplication1.dayofweek.service.DayOfWeekLocaleService;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@Slf4j
public class DayOfWeekLocaleServiceImpl implements DayOfWeekLocaleService {
    private final DayOfWeekLocaleRepository dayOfWeekLocaleRepository;

    public DayOfWeekLocaleServiceImpl(DayOfWeekLocaleRepository dayOfWeekLocaleRepository) {
        this.dayOfWeekLocaleRepository = dayOfWeekLocaleRepository;
    }

    @Transactional
    @Override
    public SuccessResponse create(CreateDayOfWeekLocaleRequest request,
                                  DayOfWeekEntity dayOfWeekEntity,
                                  LocaleEntity localeEntity) {
        if (dayOfWeekLocaleRepository.existsByDayOfWeekEntity_IdAndLocaleEntity_IdAndIsActiveAndIsDeleted(
                dayOfWeekEntity.getId(), localeEntity.getId(), true, false)) {
            throw new IllegalStateException("DayOfWeek already has a locale entry for locale id: " + localeEntity.getId());
        }

        DayOfWeekLocaleEntity entity = DayOfWeekLocaleMapper.create(request);
        dayOfWeekEntity.addDayOfWeekLocaleEntity(entity);
        localeEntity.addDayOfWeekLocaleEntity(entity);
        dayOfWeekLocaleRepository.save(entity);
        log.info("DayOfWeekLocale created with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse update(DayOfWeekLocaleEntity entity,
                                  UpdateDayOfWeekLocaleRequest request) {
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

    @Override
    public DayOfWeekLocaleEntity getEntityById(Long dayOfWeekId, Long id) {
        return dayOfWeekLocaleRepository
                .findByDayOfWeekEntity_IdAndIdAndIsActiveAndIsDeleted(dayOfWeekId, id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("DayOfWeekLocale not found with id: " + id));
    }

    @Override
    public PaginatedResponse<DayOfWeekLocaleDto> getAll(Long dayOfWeekId, String localeCode, PaginatedRequest paginatedRequest) {
        Pageable pageable = paginatedRequest.toPageable(Set.of());
        Page<@NonNull DayOfWeekLocaleDto> dtoPage = (localeCode == null || localeCode.isBlank()
                ? dayOfWeekLocaleRepository.findByDayOfWeekEntity_IdAndIsActiveAndIsDeleted(dayOfWeekId, true, false, pageable)
                : dayOfWeekLocaleRepository.findByDayOfWeekEntity_IdAndLocaleEntity_CodeContainingIgnoreCaseAndIsActiveAndIsDeleted(
                        dayOfWeekId, localeCode, true, false, pageable))
                .map(DayOfWeekLocaleMapper::toDto);
        return Pagination.buildPaginatedResponse(dtoPage);
    }
}
