package com.example.resortbackendapplication1.facility.serviceImpl;

import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.commons.utils.EntityValidator;
import com.example.resortbackendapplication1.commons.utils.Pagination;
import com.example.resortbackendapplication1.facility.dto.request.facilitygroups.CreateFacilityGroupRequest;
import com.example.resortbackendapplication1.facility.dto.request.facilitygroups.FacilityGroupFilterRequest;
import com.example.resortbackendapplication1.facility.dto.request.facilitygroups.UpdateFacilityGroupRequest;
import com.example.resortbackendapplication1.facility.dto.response.facilitygroups.FacilityGroupResponse;
import com.example.resortbackendapplication1.facility.model.dto.FacilityGroupDto;
import com.example.resortbackendapplication1.facility.model.entity.FacilityGroupEntity;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.facility.model.enums.FacilityGroupSearchField;
import com.example.resortbackendapplication1.facility.model.enums.FacilityGroupSortField;
import com.example.resortbackendapplication1.facility.model.mapper.FacilityGroupMapper;
import com.example.resortbackendapplication1.facility.repository.FacilityGroupRepository;
import com.example.resortbackendapplication1.facility.service.FacilityGroupService;
import com.example.resortbackendapplication1.facility.specification.FacilityGroupSpecification;
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
    private static final Set<String> ALLOWED_SEARCH_FIELDS = FacilityGroupSearchField.allowedFields();

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
    public PaginatedResponse<FacilityGroupDto> getAll(FacilityGroupFilterRequest request) {
        Page<@NonNull FacilityGroupDto> page = facilityGroupRepository
                .findAll(FacilityGroupSpecification.filter(request), request.toPageable(ALLOWED_SORT_FIELDS))
                .map(FacilityGroupMapper::toDto);
        return Pagination.buildPaginatedResponse(page, ALLOWED_SORT_FIELDS, ALLOWED_SEARCH_FIELDS);
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
        entity.getFacilityEntities().stream()
                .filter(f -> Boolean.TRUE.equals(f.getIsActive()) && Boolean.FALSE.equals(f.getIsDeleted()))
                .forEach(f -> {
                    f.setIsDeleted(true);
                    f.setIsActive(false);
                });
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
