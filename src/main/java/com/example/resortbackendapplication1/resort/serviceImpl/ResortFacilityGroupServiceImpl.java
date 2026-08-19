package com.example.resortbackendapplication1.resort.serviceImpl;

import com.example.resortbackendapplication1.commons.context.LocaleContext;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.commons.utils.Pagination;
import com.example.resortbackendapplication1.facility.model.entity.FacilityGroupEntity;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.resort.dto.request.resortfacilitygroup.CreateResortFacilityGroupRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortfacilitygroup.ResortFacilityGroupFilterRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortfacilitygroup.UpdateResortFacilityGroupRequest;
import com.example.resortbackendapplication1.resort.dto.response.resortfacilitygroups.ResortFacilityGroupResponse;
import com.example.resortbackendapplication1.resort.model.dto.ResortFacilityGroupDto;
import com.example.resortbackendapplication1.resort.model.entity.ResortEntity;
import com.example.resortbackendapplication1.resort.model.entity.ResortFacilityGroupEntity;
import com.example.resortbackendapplication1.resort.model.entity.ResortFacilityGroupLocaleEntity;
import com.example.resortbackendapplication1.resort.model.enums.ResortFacilityGroupSearchField;
import com.example.resortbackendapplication1.resort.model.enums.ResortFacilityGroupSortField;
import com.example.resortbackendapplication1.resort.model.mapper.ResortFacilityGroupLocaleMapper;
import com.example.resortbackendapplication1.resort.model.mapper.ResortFacilityGroupMapper;
import com.example.resortbackendapplication1.resort.repository.ResortFacilityGroupRepository;
import com.example.resortbackendapplication1.resort.service.ResortFacilityGroupService;
import com.example.resortbackendapplication1.resort.specification.ResortFacilityGroupSpecification;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@Slf4j
public class ResortFacilityGroupServiceImpl implements ResortFacilityGroupService {

    private static final Set<String> ALLOWED_SORT_FIELDS = ResortFacilityGroupSortField.allowedFields();
    private static final Set<String> ALLOWED_SEARCH_FIELDS = ResortFacilityGroupSearchField.allowedFields();

    private final ResortFacilityGroupRepository resortFacilityGroupRepository;

    public ResortFacilityGroupServiceImpl(ResortFacilityGroupRepository resortFacilityGroupRepository) {
        this.resortFacilityGroupRepository = resortFacilityGroupRepository;
    }

    @Transactional
    @Override
    public SuccessResponse create(CreateResortFacilityGroupRequest request,
                                  ResortEntity resortEntity,
                                  FacilityGroupEntity facilityGroupEntity,
                                  LocaleEntity localeEntity) {
        if (facilityGroupEntity != null && resortFacilityGroupRepository
                .existsByResortEntity_IdAndFacilityGroupEntity_IdAndIsActiveAndIsDeleted(
                        resortEntity.getId(), facilityGroupEntity.getId(), true, false)) {
            throw new IllegalStateException("Resort already has a facility group linked to platform facility group id: "
                    + facilityGroupEntity.getId());
        }

        ResortFacilityGroupEntity entity = ResortFacilityGroupMapper.create(request, resortEntity, facilityGroupEntity);

        ResortFacilityGroupLocaleEntity resortFacilityGroupLocaleEntity = ResortFacilityGroupLocaleMapper.create(request.getLocale());
        entity.addResortFacilityGroupLocaleEntity(resortFacilityGroupLocaleEntity);
        localeEntity.addResortFacilityGroupLocaleEntity(resortFacilityGroupLocaleEntity);

        resortFacilityGroupRepository.save(entity);
        log.info("ResortFacilityGroup created with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public ResortFacilityGroupEntity getEntityById(Long resortId, Long id) {
        return resortFacilityGroupRepository.findByResortEntity_IdAndIdAndIsActiveAndIsDeleted(resortId, id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("ResortFacilityGroup not found with id: " + id));
    }

    @Override
    public ResortFacilityGroupResponse getById(Long resortId, Long id) {
        ResortFacilityGroupEntity entity = getEntityById(resortId, id);
        ResortFacilityGroupDto dto = ResortFacilityGroupMapper.toDto(entity).build();
        return new ResortFacilityGroupResponse(dto);
    }

    @Override
    public PaginatedResponse<ResortFacilityGroupDto> getAll(Long resortId, ResortFacilityGroupFilterRequest request) {
        Specification<@NonNull ResortFacilityGroupEntity> specification =
                ResortFacilityGroupSpecification.filter(resortId, request, LocaleContext.getLocaleId());
        Pageable pageable = request.toPageable(ALLOWED_SORT_FIELDS, ResortFacilityGroupSortField.localeSortFields());
        Page<@NonNull ResortFacilityGroupDto> page = resortFacilityGroupRepository
                .findAll(specification, pageable)
                .map(entity -> ResortFacilityGroupMapper.toDto(entity).build());
        return Pagination.buildPaginatedResponse(page, ALLOWED_SORT_FIELDS, ALLOWED_SEARCH_FIELDS);
    }

    @Transactional
    @Override
    public SuccessResponse update(ResortFacilityGroupEntity entity, UpdateResortFacilityGroupRequest request) {
        ResortFacilityGroupMapper.update(entity, request);
        resortFacilityGroupRepository.save(entity);
        log.info("ResortFacilityGroup updated with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse delete(ResortFacilityGroupEntity entity) {
        entity.setIsDeleted(true);
        entity.setIsActive(false);
        resortFacilityGroupRepository.save(entity);
        log.info("ResortFacilityGroup soft-deleted with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }
}
