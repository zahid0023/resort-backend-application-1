package com.example.resortbackendapplication1.dayofweek.serviceImpl;

import com.example.resortbackendapplication1.commons.context.LocaleContext;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.utils.Pagination;
import com.example.resortbackendapplication1.dayofweek.dto.request.dayofweek.DayOfWeekFilterRequest;
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
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

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

    @Override
    public DayOfWeekEntity getEntityById(Long id) {
        return dayOfWeekRepository.findByIdAndIsActiveAndIsDeleted(id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("DayOfWeek not found with id: " + id));
    }

    @Override
    public DayOfWeekResponse getById(Long id) {
        DayOfWeekEntity entity = getEntityById(id);
        DayOfWeekDto dto = DayOfWeekMapper.toDto(entity).build();
        return new DayOfWeekResponse(dto);
    }

    @Override
    public PaginatedResponse<DayOfWeekDto> getAll(DayOfWeekFilterRequest request) {
        Specification<@NonNull DayOfWeekEntity> specification = DayOfWeekSpecification.filter(request, LocaleContext.getLocaleId());
        Pageable pageable = request.toPageable(ALLOWED_SORT_FIELDS, DayOfWeekSortField.localeSortFields());
        Page<@NonNull DayOfWeekDto> page = dayOfWeekRepository
                .findAll(specification, pageable)
                .map(entity -> DayOfWeekMapper.toDto(entity).build());
        return Pagination.buildPaginatedResponse(page, ALLOWED_SORT_FIELDS, ALLOWED_SEARCH_FIELDS);
    }

    @Override
    public List<DayOfWeekEntity> getAllActiveEntities() {
        return dayOfWeekRepository.findByIsActiveAndIsDeletedOrderBySortOrderAsc(true, false);
    }
}
