package com.example.resortbackendapplication1.facility.serviceImpl;

import com.example.resortbackendapplication1.commons.context.LocaleContext;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.commons.utils.Pagination;
import com.example.resortbackendapplication1.facility.dto.request.facility.CreateFacilityRequest;
import com.example.resortbackendapplication1.facility.dto.request.facility.FacilityFilterRequest;
import com.example.resortbackendapplication1.facility.dto.request.facility.UpdateFacilityRequest;
import com.example.resortbackendapplication1.facility.dto.response.facilities.FacilityResponse;
import com.example.resortbackendapplication1.facility.model.dto.FacilityDto;
import com.example.resortbackendapplication1.facility.model.dto.FacilityGroupDto;
import com.example.resortbackendapplication1.facility.model.dto.FacilityScopeDto;
import com.example.resortbackendapplication1.facility.model.entity.FacilityEntity;
import com.example.resortbackendapplication1.facility.model.entity.FacilityFacilityGroupAssignmentEntity;
import com.example.resortbackendapplication1.facility.model.entity.FacilityGroupEntity;
import com.example.resortbackendapplication1.facility.model.entity.FacilityLocaleEntity;
import com.example.resortbackendapplication1.facility.model.entity.FacilityScopeAssignmentEntity;
import com.example.resortbackendapplication1.facility.model.entity.FacilityScopeEntity;
import com.example.resortbackendapplication1.facility.model.enums.FacilitySearchField;
import com.example.resortbackendapplication1.facility.model.enums.FacilitySortField;
import com.example.resortbackendapplication1.facility.model.mapper.FacilityFacilityGroupAssignmentMapper;
import com.example.resortbackendapplication1.facility.model.mapper.FacilityGroupMapper;
import com.example.resortbackendapplication1.facility.model.mapper.FacilityLocaleMapper;
import com.example.resortbackendapplication1.facility.model.mapper.FacilityMapper;
import com.example.resortbackendapplication1.facility.model.mapper.FacilityScopeAssignmentMapper;
import com.example.resortbackendapplication1.facility.model.mapper.FacilityScopeMapper;
import com.example.resortbackendapplication1.facility.repository.FacilityRepository;
import com.example.resortbackendapplication1.facility.service.FacilityService;
import com.example.resortbackendapplication1.facility.specification.FacilitySpecification;
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
import java.util.stream.Collectors;

@Service
@Slf4j
public class FacilityServiceImpl implements FacilityService {

    private static final Set<String> ALLOWED_SORT_FIELDS = FacilitySortField.allowedFields();
    private static final Set<String> ALLOWED_SEARCH_FIELDS = FacilitySearchField.allowedFields();

    private final FacilityRepository facilityRepository;

    public FacilityServiceImpl(FacilityRepository facilityRepository) {
        this.facilityRepository = facilityRepository;
    }

    @Transactional
    @Override
    public SuccessResponse create(CreateFacilityRequest request,
                                  List<FacilityGroupEntity> facilityGroupEntities,
                                  List<FacilityScopeEntity> facilityScopeEntities,
                                  LocaleEntity localeEntity) {
        if (facilityRepository.existsByCodeAndIsActiveAndIsDeleted(request.getCode(), true, false)) {
            throw new IllegalStateException("Facility with code '" + request.getCode() + "' already exists");
        }

        FacilityEntity entity = FacilityMapper.create(request);

        for (FacilityGroupEntity facilityGroupEntity : facilityGroupEntities) {
            validateScopeCompatibility(facilityGroupEntity, facilityScopeEntities);
            FacilityFacilityGroupAssignmentEntity assignmentEntity = FacilityFacilityGroupAssignmentMapper.create();
            facilityGroupEntity.addFacilityFacilityGroupAssignmentEntity(assignmentEntity);
            entity.addFacilityFacilityGroupAssignmentEntity(assignmentEntity);
        }

        for (FacilityScopeEntity facilityScopeEntity : facilityScopeEntities) {
            FacilityScopeAssignmentEntity assignmentEntity = FacilityScopeAssignmentMapper.create();
            facilityScopeEntity.addFacilityScopeAssignmentEntity(assignmentEntity);
            entity.addFacilityScopeAssignmentEntity(assignmentEntity);
        }

        FacilityLocaleEntity facilityLocaleEntity = FacilityLocaleMapper.create(request.getLocale());
        localeEntity.addFacilityLocaleEntity(facilityLocaleEntity);

        entity.addFacilityLocaleEntity(facilityLocaleEntity);

        facilityRepository.save(entity);
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
        FacilityDto dto = FacilityMapper.toDto(entity)
                .facilityGroups(mapFacilityGroups(entity))
                .facilityScopes(mapFacilityScopes(entity))
                .build();
        return new FacilityResponse(dto);
    }

    @Override
    public PaginatedResponse<FacilityDto> getAll(FacilityFilterRequest request) {
        Specification<@NonNull FacilityEntity> specification = FacilitySpecification.filter(request, LocaleContext.getLocaleId());
        Pageable pageable = request.toPageable(ALLOWED_SORT_FIELDS, FacilitySortField.localeSortFields());
        Page<@NonNull FacilityDto> page = facilityRepository
                .findAll(specification, pageable)
                .map(entity -> FacilityMapper.toDto(entity)
                        .facilityGroups(mapFacilityGroups(entity))
                        .facilityScopes(mapFacilityScopes(entity))
                        .build());
        return Pagination.buildPaginatedResponse(page, ALLOWED_SORT_FIELDS, ALLOWED_SEARCH_FIELDS);
    }

    private void validateScopeCompatibility(FacilityGroupEntity facilityGroupEntity, List<FacilityScopeEntity> facilityScopeEntities) {
        Set<Long> allowedScopeIds = facilityGroupEntity.getFacilityGroupScopeAssignmentEntities().stream()
                .filter(assignment -> Boolean.TRUE.equals(assignment.getIsActive())
                        && Boolean.FALSE.equals(assignment.getIsDeleted()))
                .map(assignment -> assignment.getFacilityScopeEntity().getId())
                .collect(Collectors.toSet());

        List<String> unsupportedCodes = facilityScopeEntities.stream()
                .filter(facilityScopeEntity -> !allowedScopeIds.contains(facilityScopeEntity.getId()))
                .map(FacilityScopeEntity::getCode)
                .toList();

        if (!unsupportedCodes.isEmpty()) {
            throw new IllegalStateException("FacilityGroup '" + facilityGroupEntity.getCode()
                    + "' is not scoped to: " + unsupportedCodes);
        }
    }

    private List<FacilityGroupDto> mapFacilityGroups(FacilityEntity entity) {
        return entity.getFacilityFacilityGroupAssignmentEntities().stream()
                .filter(assignment -> Boolean.TRUE.equals(assignment.getIsActive())
                        && Boolean.FALSE.equals(assignment.getIsDeleted()))
                .map(FacilityFacilityGroupAssignmentEntity::getFacilityGroupEntity)
                .map(facilityGroupEntity -> FacilityGroupMapper.toDto(facilityGroupEntity).build())
                .toList();
    }

    private List<FacilityScopeDto> mapFacilityScopes(FacilityEntity entity) {
        return entity.getFacilityScopeAssignmentEntities().stream()
                .filter(assignment -> Boolean.TRUE.equals(assignment.getIsActive())
                        && Boolean.FALSE.equals(assignment.getIsDeleted()))
                .map(FacilityScopeAssignmentEntity::getFacilityScopeEntity)
                .map(facilityScopeEntity -> FacilityScopeMapper.toDto(facilityScopeEntity).build())
                .toList();
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
    public SuccessResponse delete(FacilityEntity entity) {
        entity.setIsDeleted(true);
        entity.setIsActive(false);

        entity.getFacilityLocaleEntities().forEach(localeEntity -> {
            localeEntity.setIsDeleted(true);
            localeEntity.setIsActive(false);
        });

        facilityRepository.save(entity);
        log.info("Facility soft-deleted with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }
}
