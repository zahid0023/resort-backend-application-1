package com.example.resortbackendapplication1.facility.serviceImpl;

import com.example.resortbackendapplication1.commons.context.LocaleContext;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.commons.utils.Pagination;
import com.example.resortbackendapplication1.facility.dto.request.facilityscope.CreateFacilityScopeRequest;
import com.example.resortbackendapplication1.facility.dto.request.facilityscope.FacilityScopeFilterRequest;
import com.example.resortbackendapplication1.facility.dto.request.facilityscope.UpdateFacilityScopeRequest;
import com.example.resortbackendapplication1.facility.dto.response.facilityscopes.FacilityScopeCountResponse;
import com.example.resortbackendapplication1.facility.dto.response.facilityscopes.FacilityScopeResponse;
import com.example.resortbackendapplication1.facility.model.dto.FacilityScopeDto;
import com.example.resortbackendapplication1.facility.model.entity.FacilityScopeEntity;
import com.example.resortbackendapplication1.facility.model.entity.FacilityScopeLocaleEntity;
import com.example.resortbackendapplication1.facility.model.enums.FacilityScopeSearchField;
import com.example.resortbackendapplication1.facility.model.enums.FacilityScopeSortField;
import com.example.resortbackendapplication1.facility.model.mapper.FacilityScopeLocaleMapper;
import com.example.resortbackendapplication1.facility.model.mapper.FacilityScopeMapper;
import com.example.resortbackendapplication1.facility.repository.FacilityScopeRepository;
import com.example.resortbackendapplication1.facility.service.FacilityScopeService;
import com.example.resortbackendapplication1.facility.specification.FacilityScopeSpecification;
import com.example.resortbackendapplication1.commons.utils.EntityValidator;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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

    @Transactional
    @Override
    public SuccessResponse create(CreateFacilityScopeRequest request, LocaleEntity localeEntity) {
        if (facilityScopeRepository.existsByCodeAndIsActiveAndIsDeleted(request.getCode(), true, false)) {
            throw new IllegalStateException("FacilityScope with code '" + request.getCode() + "' already exists");
        }

        FacilityScopeEntity entity = FacilityScopeMapper.create(request);

        FacilityScopeLocaleEntity facilityScopeLocaleEntity = FacilityScopeLocaleMapper.create(request.getLocale());
        localeEntity.addFacilityScopeLocaleEntity(facilityScopeLocaleEntity);
        entity.addFacilityScopeLocaleEntity(facilityScopeLocaleEntity);

        facilityScopeRepository.save(entity);
        log.info("FacilityScope created with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public FacilityScopeEntity getEntityById(Long id) {
        return facilityScopeRepository.findByIdAndIsActiveAndIsDeleted(id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("FacilityScope not found with id: " + id));
    }

    @Override
    public List<FacilityScopeEntity> getAll(Set<Long> ids) {
        List<FacilityScopeEntity> facilityScopeEntities = facilityScopeRepository.findAllByIdInAndIsActiveAndIsDeleted(ids, true, false);
        EntityValidator.validateAllFound(ids, facilityScopeEntities, FacilityScopeEntity::getId, "FacilityScope");
        return facilityScopeEntities;
    }

    @Override
    public List<FacilityScopeEntity> getAllByCodes(Set<String> codes) {
        List<FacilityScopeEntity> facilityScopeEntities = facilityScopeRepository.findAllByCodeInAndIsActiveAndIsDeleted(codes, true, false);
        EntityValidator.validateAllFound(codes, facilityScopeEntities, FacilityScopeEntity::getCode, "FacilityScope");
        return facilityScopeEntities;
    }

    @Override
    public FacilityScopeResponse getById(Long id) {
        FacilityScopeEntity entity = getEntityById(id);
        FacilityScopeDto dto = FacilityScopeMapper.toDto(entity).build();
        return new FacilityScopeResponse(dto);
    }

    @Override
    public PaginatedResponse<FacilityScopeDto> getAll(FacilityScopeFilterRequest request) {
        Specification<@NonNull FacilityScopeEntity> specification =
                FacilityScopeSpecification.filter(request, LocaleContext.getLocaleId());
        Pageable pageable = request.toPageable(ALLOWED_SORT_FIELDS, FacilityScopeSortField.localeSortFields());
        Page<@NonNull FacilityScopeDto> page = facilityScopeRepository
                .findAll(specification, pageable)
                .map(entity -> FacilityScopeMapper.toDto(entity).build());
        return Pagination.buildPaginatedResponse(page, ALLOWED_SORT_FIELDS, ALLOWED_SEARCH_FIELDS);
    }

    @Override
    public FacilityScopeCountResponse getActiveCount() {
        List<String> codes = facilityScopeRepository.findCodeByIsActiveAndIsDeleted(true, false);
        return new FacilityScopeCountResponse((long) codes.size(), codes);
    }

    @Transactional
    @Override
    public SuccessResponse update(FacilityScopeEntity entity, UpdateFacilityScopeRequest request) {
        FacilityScopeMapper.update(entity, request);
        facilityScopeRepository.save(entity);
        log.info("FacilityScope updated with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse delete(FacilityScopeEntity entity) {
        entity.setIsDeleted(true);
        entity.setIsActive(false);

        entity.getFacilityScopeLocaleEntities().forEach(localeEntity -> {
            localeEntity.setIsDeleted(true);
            localeEntity.setIsActive(false);
        });

        facilityScopeRepository.save(entity);
        log.info("FacilityScope soft-deleted with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }
}
