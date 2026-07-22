package com.example.resortbackendapplication1.dayofweek.serviceImpl;

import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.commons.utils.EntityValidator;
import com.example.resortbackendapplication1.commons.utils.Pagination;
import com.example.resortbackendapplication1.dayofweek.dto.request.dayofweek.DayOfWeekFilterRequest;
import com.example.resortbackendapplication1.dayofweek.dto.request.dayofweek.UpdateDayOfWeekRequest;
import com.example.resortbackendapplication1.dayofweek.dto.response.daysofweek.DayOfWeekResponse;
import com.example.resortbackendapplication1.dayofweek.model.dto.DayOfWeekDto;
import com.example.resortbackendapplication1.dayofweek.model.entity.DayOfWeekEntity;
import com.example.resortbackendapplication1.dayofweek.model.enums.DayOfWeekSearchField;
import com.example.resortbackendapplication1.dayofweek.model.enums.DayOfWeekSortField;
import com.example.resortbackendapplication1.dayofweek.model.mapper.DayOfWeekMapper;
import com.example.resortbackendapplication1.dayofweek.repository.DayOfWeekRepository;
import com.example.resortbackendapplication1.dayofweek.service.DayOfWeekService;
import com.example.resortbackendapplication1.dayofweek.specification.DayOfWeekSpecification;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class DayOfWeekServiceImpl implements DayOfWeekService {

    private static final Set<String> ALLOWED_SORT_FIELDS = DayOfWeekSortField.allowedFields();
    private static final Set<String> ALLOWED_SEARCH_FIELDS = DayOfWeekSearchField.allowedFields();

    private final DayOfWeekRepository dayOfWeekRepository;

    public DayOfWeekServiceImpl(DayOfWeekRepository dayOfWeekRepository) {
        this.dayOfWeekRepository = dayOfWeekRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public DayOfWeekResponse getById(Long id) {
        DayOfWeekEntity entity = getEntityById(id);
        DayOfWeekDto dto = DayOfWeekMapper.toDto(entity);
        return new DayOfWeekResponse(dto);
    }

    @Transactional(readOnly = true)
    @Override
    public PaginatedResponse<DayOfWeekDto> getAll(DayOfWeekFilterRequest request) {
        Page<@NonNull DayOfWeekDto> page = dayOfWeekRepository
                .findAll(DayOfWeekSpecification.filter(request), request.toPageable(ALLOWED_SORT_FIELDS))
                .map(DayOfWeekMapper::toDto);
        return Pagination.buildPaginatedResponse(page, ALLOWED_SORT_FIELDS, ALLOWED_SEARCH_FIELDS);
    }

    @Transactional
    @Override
    public SuccessResponse update(DayOfWeekEntity entity, UpdateDayOfWeekRequest request) {
        DayOfWeekMapper.update(entity, request);
        dayOfWeekRepository.save(entity);
        log.info("DayOfWeek updated with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional(readOnly = true)
    @Override
    public DayOfWeekEntity getEntityById(Long id) {
        return dayOfWeekRepository.findByIdAndIsActiveAndIsDeleted(id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("DayOfWeek not found with id: " + id));
    }

    @Transactional(readOnly = true)
    @Override
    public List<DayOfWeekEntity> getAll(Set<Long> ids) {
        List<DayOfWeekEntity> entities = dayOfWeekRepository.findAllByIdInAndIsActiveAndIsDeleted(ids, true, false);
        EntityValidator.validateAllFound(ids, entities, DayOfWeekEntity::getId, "DayOfWeek");
        return entities;
    }
}
