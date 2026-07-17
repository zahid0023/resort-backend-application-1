package com.example.resortbackendapplication1.resort.serviceImpl;

import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.commons.utils.EntityValidator;
import com.example.resortbackendapplication1.commons.utils.Pagination;
import com.example.resortbackendapplication1.facility.model.entity.FacilityGroupEntity;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.resort.dto.request.resortfacilitygroup.CreateResortFacilityGroupRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortfacilitygroup.ResortFacilityGroupFilterRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortfacilitygroup.UpdateResortFacilityGroupRequest;
import com.example.resortbackendapplication1.resort.dto.response.ResortFacilityGroupResponse;
import com.example.resortbackendapplication1.resort.model.dto.ResortFacilityGroupDto;
import com.example.resortbackendapplication1.resort.model.entity.ResortEntity;
import com.example.resortbackendapplication1.resort.model.entity.ResortFacilityGroupEntity;
import com.example.resortbackendapplication1.resort.model.enums.ResortFacilityGroupSortField;
import com.example.resortbackendapplication1.resort.model.mapper.ResortFacilityGroupMapper;
import com.example.resortbackendapplication1.resort.repository.ResortFacilityGroupRepository;
import com.example.resortbackendapplication1.resort.service.ResortFacilityGroupService;
import com.example.resortbackendapplication1.resort.specification.ResortFacilityGroupSpecification;
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
public class ResortFacilityGroupServiceImpl implements ResortFacilityGroupService {

    private static final Set<String> ALLOWED_SORT_FIELDS = ResortFacilityGroupSortField.allowedFields();
    private static final Set<String> ALLOWED_SEARCH_FIELDS = Set.of();

    private final ResortFacilityGroupRepository resortFacilityGroupRepository;

    public ResortFacilityGroupServiceImpl(ResortFacilityGroupRepository resortFacilityGroupRepository) {
        this.resortFacilityGroupRepository = resortFacilityGroupRepository;
    }

    @Transactional
    @Override
    public SuccessResponse create(CreateResortFacilityGroupRequest request,
                                  ResortEntity resortEntity,
                                  FacilityGroupEntity facilityGroupEntity,
                                  Map<Long, LocaleEntity> localeEntityMap) {
        if (facilityGroupEntity != null &&
                resortFacilityGroupRepository.existsByResortEntity_IdAndFacilityGroupEntity_IdAndIsDeleted(
                        resortEntity.getId(), facilityGroupEntity.getId(), false)) {
            throw new IllegalArgumentException(
                    "The facility group is already assigned to the resort.");
        }
        ResortFacilityGroupEntity entity = ResortFacilityGroupMapper.create(request, resortEntity, facilityGroupEntity, localeEntityMap);
        resortFacilityGroupRepository.save(entity);
        log.info("ResortFacilityGroup created with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public ResortFacilityGroupEntity getEntityById(Long id) {
        return resortFacilityGroupRepository.findByIdAndIsActiveAndIsDeleted(id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("ResortFacilityGroup not found with id: " + id));
    }

    @Override
    public ResortFacilityGroupResponse getById(Long id) {
        ResortFacilityGroupEntity entity = getEntityById(id);
        return new ResortFacilityGroupResponse(ResortFacilityGroupMapper.toDto(entity));
    }

    @Override
    public PaginatedResponse<ResortFacilityGroupDto> getAll(ResortFacilityGroupFilterRequest request, Long resortId) {
        Page<@NonNull ResortFacilityGroupDto> page = resortFacilityGroupRepository
                .findAll(ResortFacilityGroupSpecification.filter(request, resortId), request.toPageable(ALLOWED_SORT_FIELDS))
                .map(ResortFacilityGroupMapper::toDto);
        return Pagination.buildPaginatedResponse(page, ALLOWED_SORT_FIELDS, ALLOWED_SEARCH_FIELDS);
    }

    @Transactional
    @Override
    public SuccessResponse update(ResortFacilityGroupEntity entity,
                                  UpdateResortFacilityGroupRequest request,
                                  FacilityGroupEntity facilityGroupEntity) {
        if (facilityGroupEntity != null &&
                resortFacilityGroupRepository.existsByResortEntity_IdAndFacilityGroupEntity_IdAndIdNotAndIsDeleted(
                        entity.getResortEntity().getId(), facilityGroupEntity.getId(), entity.getId(), false)) {
            throw new IllegalArgumentException(
                    "This facility group is already assigned to the resort.");
        }
        ResortFacilityGroupMapper.update(entity, request, facilityGroupEntity);
        resortFacilityGroupRepository.save(entity);
        log.info("ResortFacilityGroup updated with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse delete(Long id) {
        ResortFacilityGroupEntity entity = getEntityById(id);
        entity.getResortFacilityEntities().stream()
                .filter(f -> Boolean.TRUE.equals(f.getIsActive()) && Boolean.FALSE.equals(f.getIsDeleted()))
                .forEach(f -> {
                    f.setIsDeleted(true);
                    f.setIsActive(false);
                });
        entity.setIsDeleted(true);
        entity.setIsActive(false);
        resortFacilityGroupRepository.save(entity);
        log.info("ResortFacilityGroup soft-deleted with id: {}", id);
        return new SuccessResponse(true, id);
    }

    @Override
    public List<ResortFacilityGroupEntity> getAll(Set<Long> ids) {
        List<ResortFacilityGroupEntity> entities = resortFacilityGroupRepository
                .findAllByIdInAndIsActiveAndIsDeleted(ids, true, false);
        EntityValidator.validateAllFound(ids, entities, ResortFacilityGroupEntity::getId, "ResortFacilityGroup");
        return entities;
    }
}
