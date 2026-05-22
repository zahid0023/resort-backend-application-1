package com.example.resortbackendapplication1.serviceImpl;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.dto.request.facilitygroups.CreateFacilityGroupRequest;
import com.example.resortbackendapplication1.dto.request.facilitygroups.UpdateFacilityGroupRequest;
import com.example.resortbackendapplication1.dto.response.facilitygroups.FacilityGroupResponse;
import com.example.resortbackendapplication1.model.dto.FacilityGroupDto;
import com.example.resortbackendapplication1.model.entity.FacilityGroupEntity;
import com.example.resortbackendapplication1.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.model.enums.FacilityGroupSortField;
import com.example.resortbackendapplication1.model.mapper.FacilityGroupMapper;
import com.example.resortbackendapplication1.model.projection.FacilityGroupSummary;
import com.example.resortbackendapplication1.repository.FacilityGroupRepository;
import com.example.resortbackendapplication1.service.FacilityGroupService;
import com.example.resortbackendapplication1.utils.Pagination;
import com.example.resortbackendapplication1.validation.EntityValidator;
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
public class FacilityGroupServiceImpl implements FacilityGroupService {

    private static final Set<String> ALLOWED_SORT_FIELDS = FacilityGroupSortField.allowedFields();

    private final FacilityGroupRepository facilityGroupRepository;

    public FacilityGroupServiceImpl(FacilityGroupRepository facilityGroupRepository) {
        this.facilityGroupRepository = facilityGroupRepository;
    }

    @Transactional
    @Override
    public SuccessResponse create(CreateFacilityGroupRequest request, Map<Long, LocaleEntity> localeEntityMap) {
        FacilityGroupEntity entity = FacilityGroupMapper.create(request, localeEntityMap);
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
    public FacilityGroupResponse getById(Long id) {
        FacilityGroupEntity entity = getEntityById(id);
        FacilityGroupDto dto = FacilityGroupMapper.toDto(entity);
        return new FacilityGroupResponse(dto);
    }

    @Override
    public PaginatedResponse<FacilityGroupSummary> getAll(PaginatedRequest request) {
        Page<@NonNull FacilityGroupSummary> page = facilityGroupRepository.findAllByIsActiveAndIsDeleted(
                true, false, request.toPageable(ALLOWED_SORT_FIELDS)
        );
        return Pagination.buildPaginatedResponse(page);
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
    public SuccessResponse delete(Long id) {
        FacilityGroupEntity entity = getEntityById(id);
        entity.setIsDeleted(true);
        entity.setIsActive(false);
        facilityGroupRepository.save(entity);
        log.info("FacilityGroup soft-deleted with id: {}", id);
        return new SuccessResponse(true, id);
    }

    @Override
    public List<FacilityGroupEntity> getAll(Set<Long> ids) {
        List<FacilityGroupEntity> entities = facilityGroupRepository.findAllByIdInAndIsActiveAndIsDeleted(ids, true, false);
        EntityValidator.validateAllFound(ids, entities, FacilityGroupEntity::getId, "FacilityGroup");
        return entities;
    }
}
