package com.example.resortbackendapplication1.facility.serviceImpl;

import com.example.resortbackendapplication1.commons.context.LocaleContext;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.commons.utils.Pagination;
import com.example.resortbackendapplication1.facility.dto.request.facilitygroup.CreateFacilityGroupRequest;
import com.example.resortbackendapplication1.facility.dto.request.facilitygroup.FacilityGroupFilterRequest;
import com.example.resortbackendapplication1.facility.dto.request.facilitygroup.UpdateFacilityGroupRequest;
import com.example.resortbackendapplication1.facility.dto.response.facilitygroups.FacilityGroupResponse;
import com.example.resortbackendapplication1.facility.model.dto.FacilityGroupDto;
import com.example.resortbackendapplication1.facility.model.dto.FacilityScopeDto;
import com.example.resortbackendapplication1.facility.model.entity.FacilityGroupEntity;
import com.example.resortbackendapplication1.facility.model.entity.FacilityGroupLocaleEntity;
import com.example.resortbackendapplication1.facility.model.entity.FacilityGroupScopeAssignmentEntity;
import com.example.resortbackendapplication1.facility.model.entity.FacilityScopeEntity;
import com.example.resortbackendapplication1.facility.model.enums.FacilityGroupSearchField;
import com.example.resortbackendapplication1.facility.model.enums.FacilityGroupSortField;
import com.example.resortbackendapplication1.facility.model.mapper.FacilityGroupLocaleMapper;
import com.example.resortbackendapplication1.facility.model.mapper.FacilityGroupMapper;
import com.example.resortbackendapplication1.facility.model.mapper.FacilityGroupScopeAssignmentMapper;
import com.example.resortbackendapplication1.facility.model.mapper.FacilityScopeMapper;
import com.example.resortbackendapplication1.facility.repository.FacilityGroupRepository;
import com.example.resortbackendapplication1.facility.service.FacilityGroupService;
import com.example.resortbackendapplication1.facility.specification.FacilityGroupSpecification;
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
public class FacilityGroupServiceImpl implements FacilityGroupService {

    private static final Set<String> ALLOWED_SORT_FIELDS = FacilityGroupSortField.allowedFields();
    private static final Set<String> ALLOWED_SEARCH_FIELDS = FacilityGroupSearchField.allowedFields();

    private final FacilityGroupRepository facilityGroupRepository;

    public FacilityGroupServiceImpl(FacilityGroupRepository facilityGroupRepository) {
        this.facilityGroupRepository = facilityGroupRepository;
    }

    @Transactional
    @Override
    public SuccessResponse create(CreateFacilityGroupRequest request,
                                  List<FacilityScopeEntity> facilityScopeEntities,
                                  LocaleEntity localeEntity) {
        if (facilityGroupRepository.existsByCodeAndIsActiveAndIsDeleted(request.getCode(), true, false)) {
            throw new IllegalStateException("FacilityGroup with code '" + request.getCode() + "' already exists");
        }

        FacilityGroupEntity entity = FacilityGroupMapper.create(request);

        for (FacilityScopeEntity facilityScopeEntity : facilityScopeEntities) {
            FacilityGroupScopeAssignmentEntity assignmentEntity = FacilityGroupScopeAssignmentMapper.create();
            facilityScopeEntity.addFacilityGroupScopeAssignmentEntity(assignmentEntity);
            entity.addFacilityGroupScopeAssignmentEntity(assignmentEntity);
        }

        FacilityGroupLocaleEntity facilityGroupLocaleEntity = FacilityGroupLocaleMapper.create(request.getLocale());
        localeEntity.addFacilityGroupLocaleEntity(facilityGroupLocaleEntity);
        entity.addFacilityGroupLocaleEntity(facilityGroupLocaleEntity);

        facilityGroupRepository.save(entity);
        log.info("FacilityGroup created with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public FacilityGroupEntity getEntityById(Long id) {
        return facilityGroupRepository.findByIdAndIsActiveAndIsDeleted(id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("FacilityGroup not found with id: " + id));
    }

    @Override
    public List<FacilityGroupEntity> getAll(Set<Long> ids) {
        List<FacilityGroupEntity> facilityGroupEntities = facilityGroupRepository.findAllByIdInAndIsActiveAndIsDeleted(ids, true, false);
        EntityValidator.validateAllFound(ids, facilityGroupEntities, FacilityGroupEntity::getId, "FacilityGroup");
        return facilityGroupEntities;
    }

    @Override
    public FacilityGroupResponse getById(Long id) {
        FacilityGroupEntity entity = getEntityById(id);
        FacilityGroupDto dto = FacilityGroupMapper.toDto(entity)
                .facilityScopes(mapFacilityScopes(entity))
                .build();
        return new FacilityGroupResponse(dto);
    }

    @Override
    public PaginatedResponse<FacilityGroupDto> getAll(FacilityGroupFilterRequest request) {
        Specification<@NonNull FacilityGroupEntity> specification =
                FacilityGroupSpecification.filter(request, LocaleContext.getLocaleId());
        Pageable pageable = request.toPageable(ALLOWED_SORT_FIELDS, FacilityGroupSortField.localeSortFields());
        Page<@NonNull FacilityGroupDto> page = facilityGroupRepository
                .findAll(specification, pageable)
                .map(entity -> FacilityGroupMapper.toDto(entity)
                        .facilityScopes(mapFacilityScopes(entity))
                        .build());
        return Pagination.buildPaginatedResponse(page, ALLOWED_SORT_FIELDS, ALLOWED_SEARCH_FIELDS);
    }

    private List<FacilityScopeDto> mapFacilityScopes(FacilityGroupEntity entity) {
        return entity.getFacilityGroupScopeAssignmentEntities().stream()
                .filter(assignment -> Boolean.TRUE.equals(assignment.getIsActive())
                        && Boolean.FALSE.equals(assignment.getIsDeleted()))
                .map(FacilityGroupScopeAssignmentEntity::getFacilityScopeEntity)
                .map(facilityScopeEntity -> FacilityScopeMapper.toDto(facilityScopeEntity).build())
                .toList();
    }

    @Transactional
    @Override
    public SuccessResponse update(FacilityGroupEntity entity, UpdateFacilityGroupRequest request) {
        FacilityGroupMapper.update(entity, request);
        facilityGroupRepository.save(entity);
        log.info("FacilityGroup updated with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse delete(FacilityGroupEntity entity) {
        entity.setIsDeleted(true);
        entity.setIsActive(false);
        facilityGroupRepository.save(entity);
        log.info("FacilityGroup soft-deleted with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }
}
