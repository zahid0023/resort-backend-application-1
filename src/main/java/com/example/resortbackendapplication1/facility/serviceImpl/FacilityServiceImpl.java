package com.example.resortbackendapplication1.facility.serviceImpl;

import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.commons.utils.EntityValidator;
import com.example.resortbackendapplication1.commons.utils.Pagination;
import com.example.resortbackendapplication1.facility.dto.request.facilities.CreateFacilityRequest;
import com.example.resortbackendapplication1.facility.dto.request.facilities.FacilityFilterRequest;
import com.example.resortbackendapplication1.facility.dto.request.facilities.UpdateFacilityRequest;
import com.example.resortbackendapplication1.facility.dto.response.facilities.FacilityResponse;
import com.example.resortbackendapplication1.facility.model.dto.FacilityDto;
import com.example.resortbackendapplication1.facility.model.entity.FacilityEntity;
import com.example.resortbackendapplication1.facility.model.entity.FacilityGroupEntity;
import com.example.resortbackendapplication1.facility.model.entity.FacilityScopeAssignmentEntity;
import com.example.resortbackendapplication1.facility.model.entity.FacilityScopeEntity;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.facility.model.enums.FacilitySearchField;
import com.example.resortbackendapplication1.facility.model.enums.FacilityScopeCode;
import com.example.resortbackendapplication1.facility.model.enums.FacilitySortField;
import com.example.resortbackendapplication1.facility.model.mapper.FacilityMapper;
import com.example.resortbackendapplication1.facility.model.mapper.FacilityScopeAssignmentMapper;
import com.example.resortbackendapplication1.facility.repository.FacilityRepository;
import com.example.resortbackendapplication1.facility.repository.FacilityScopeAssignmentRepository;
import com.example.resortbackendapplication1.facility.service.FacilityService;
import com.example.resortbackendapplication1.facility.specification.FacilitySpecification;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@Slf4j
public class FacilityServiceImpl implements FacilityService {

    private static final Set<String> ALLOWED_SORT_FIELDS = FacilitySortField.allowedFields();
    private static final Set<String> ALLOWED_SEARCH_FIELDS = FacilitySearchField.allowedFields();

    private final FacilityRepository facilityRepository;
    private final FacilityScopeAssignmentRepository scopeAssignmentRepository;

    public FacilityServiceImpl(FacilityRepository facilityRepository,
                                FacilityScopeAssignmentRepository scopeAssignmentRepository) {
        this.facilityRepository = facilityRepository;
        this.scopeAssignmentRepository = scopeAssignmentRepository;
    }

    @Transactional
    @Override
    public SuccessResponse create(CreateFacilityRequest request,
                                  Map<Long, LocaleEntity> localeEntityMap,
                                  FacilityGroupEntity facilityGroupEntity,
                                  List<FacilityScopeEntity> scopeEntities) {
        FacilityEntity entity = FacilityMapper.create(request, facilityGroupEntity, localeEntityMap);
        facilityRepository.save(entity);

        List<FacilityScopeAssignmentEntity> assignments = scopeEntities.stream()
                .map(scope -> FacilityScopeAssignmentMapper.create(entity, scope))
                .toList();
        scopeAssignmentRepository.saveAll(assignments);

        log.info("Facility created with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public FacilityEntity getEntityById(Long id) {
        return facilityRepository.findByIdAndIsActiveAndIsDeleted(id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("Facility not found with id: " + id));
    }

    @Override
    public FacilityResponse getById(Long id) {
        FacilityEntity entity = getEntityById(id);
        FacilityDto dto = FacilityMapper.toDto(entity);
        return new FacilityResponse(dto);
    }

    @Override
    public PaginatedResponse<FacilityDto> getAll(FacilityFilterRequest request, Long facilityGroupId, FacilityScopeCode scopeCode) {
        Page<@NonNull FacilityDto> page = facilityRepository
                .findAll(FacilitySpecification.filter(request, facilityGroupId, scopeCode), request.toPageable(ALLOWED_SORT_FIELDS))
                .map(FacilityMapper::toDto);
        return Pagination.buildPaginatedResponse(page, ALLOWED_SORT_FIELDS, ALLOWED_SEARCH_FIELDS);
    }

    @Transactional
    @Override
    public SuccessResponse update(FacilityEntity entity, UpdateFacilityRequest request) {
        FacilityMapper.update(entity, request);
        facilityRepository.save(entity);
        log.info("Facility updated with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse delete(Long id) {
        FacilityEntity entity = getEntityById(id);
        entity.setIsDeleted(true);
        entity.setIsActive(false);
        facilityRepository.save(entity);
        log.info("Facility soft-deleted with id: {}", id);
        return new SuccessResponse(true, id);
    }

    @Override
    public List<FacilityEntity> getAll(Set<Long> ids) {
        List<FacilityEntity> entities = facilityRepository.findAllByIdInAndIsActiveAndIsDeleted(ids, true, false);
        EntityValidator.validateAllFound(ids, entities, FacilityEntity::getId, "Facility");
        return entities;
    }
}
