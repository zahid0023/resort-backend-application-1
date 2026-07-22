package com.example.resortbackendapplication1.facility.serviceImpl;

import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.commons.utils.EntityValidator;
import com.example.resortbackendapplication1.commons.utils.Pagination;
import com.example.resortbackendapplication1.facility.dto.request.facilityscopes.FacilityScopeFilterRequest;
import com.example.resortbackendapplication1.facility.dto.request.facilityscopes.UpdateFacilityScopeRequest;
import com.example.resortbackendapplication1.facility.dto.response.facilityscopes.FacilityScopeResponse;
import com.example.resortbackendapplication1.facility.model.dto.FacilityScopeDto;
import com.example.resortbackendapplication1.facility.model.entity.FacilityScopeEntity;
import com.example.resortbackendapplication1.facility.model.enums.FacilityScopeSearchField;
import com.example.resortbackendapplication1.facility.model.enums.FacilityScopeSortField;
import com.example.resortbackendapplication1.facility.model.mapper.FacilityScopeMapper;
import com.example.resortbackendapplication1.facility.repository.FacilityScopeRepository;
import com.example.resortbackendapplication1.facility.service.FacilityScopeService;
import com.example.resortbackendapplication1.facility.specification.FacilityScopeSpecification;
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
public class FacilityScopeServiceImpl implements FacilityScopeService {

    private static final Set<String> ALLOWED_SORT_FIELDS = FacilityScopeSortField.allowedFields();
    private static final Set<String> ALLOWED_SEARCH_FIELDS = FacilityScopeSearchField.allowedFields();

    private final FacilityScopeRepository facilityScopeRepository;

    public FacilityScopeServiceImpl(FacilityScopeRepository facilityScopeRepository) {
        this.facilityScopeRepository = facilityScopeRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public FacilityScopeEntity getEntityById(Long id) {
        return facilityScopeRepository.findByIdAndIsActiveAndIsDeleted(id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("FacilityScope not found with id: " + id));
    }

    @Transactional(readOnly = true)
    @Override
    public FacilityScopeResponse getById(Long id) {
        FacilityScopeEntity entity = getEntityById(id);
        FacilityScopeDto dto = FacilityScopeMapper.toDto(entity);
        return new FacilityScopeResponse(dto);
    }

    @Transactional(readOnly = true)
    @Override
    public PaginatedResponse<FacilityScopeDto> getAll(FacilityScopeFilterRequest request) {
        Page<@NonNull FacilityScopeDto> page = facilityScopeRepository
                .findAll(FacilityScopeSpecification.filter(request), request.toPageable(ALLOWED_SORT_FIELDS))
                .map(FacilityScopeMapper::toDto);
        return Pagination.buildPaginatedResponse(page, ALLOWED_SORT_FIELDS, ALLOWED_SEARCH_FIELDS);
    }

    @Transactional
    @Override
    public SuccessResponse update(FacilityScopeEntity entity, UpdateFacilityScopeRequest request) {
        FacilityScopeMapper.update(entity, request);
        facilityScopeRepository.save(entity);
        log.info("FacilityScope updated with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional(readOnly = true)
    @Override
    public List<FacilityScopeEntity> getAll(Set<Long> ids) {
        List<FacilityScopeEntity> entities = facilityScopeRepository.findAllByIdInAndIsActiveAndIsDeleted(ids, true, false);
        EntityValidator.validateAllFound(ids, entities, FacilityScopeEntity::getId, "FacilityScope");
        return entities;
    }
}
