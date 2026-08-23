package com.example.resortbackendapplication1.resort.serviceImpl;

import com.example.resortbackendapplication1.commons.context.LocaleContext;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.commons.utils.Pagination;
import com.example.resortbackendapplication1.facility.model.entity.FacilityGroupEntity;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.resort.dto.request.resortroomcategoryfacilitygroup.CreateResortRoomCategoryFacilityGroupRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortroomcategoryfacilitygroup.ResortRoomCategoryFacilityGroupFilterRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortroomcategoryfacilitygroup.UpdateResortRoomCategoryFacilityGroupRequest;
import com.example.resortbackendapplication1.resort.dto.response.resortroomcategoryfacilitygroups.ResortRoomCategoryFacilityGroupResponse;
import com.example.resortbackendapplication1.resort.model.dto.ResortRoomCategoryFacilityGroupDto;
import com.example.resortbackendapplication1.resort.model.entity.ResortRoomCategoryEntity;
import com.example.resortbackendapplication1.resort.model.entity.ResortRoomCategoryFacilityGroupEntity;
import com.example.resortbackendapplication1.resort.model.entity.ResortRoomCategoryFacilityGroupLocaleEntity;
import com.example.resortbackendapplication1.resort.model.enums.ResortRoomCategoryFacilityGroupSearchField;
import com.example.resortbackendapplication1.resort.model.enums.ResortRoomCategoryFacilityGroupSortField;
import com.example.resortbackendapplication1.resort.model.mapper.ResortRoomCategoryFacilityGroupLocaleMapper;
import com.example.resortbackendapplication1.resort.model.mapper.ResortRoomCategoryFacilityGroupMapper;
import com.example.resortbackendapplication1.resort.repository.ResortRoomCategoryFacilityGroupRepository;
import com.example.resortbackendapplication1.resort.service.ResortRoomCategoryFacilityGroupService;
import com.example.resortbackendapplication1.resort.specification.ResortRoomCategoryFacilityGroupSpecification;
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
public class ResortRoomCategoryFacilityGroupServiceImpl implements ResortRoomCategoryFacilityGroupService {

    private static final Set<String> ALLOWED_SORT_FIELDS = ResortRoomCategoryFacilityGroupSortField.allowedFields();
    private static final Set<String> ALLOWED_SEARCH_FIELDS = ResortRoomCategoryFacilityGroupSearchField.allowedFields();

    private final ResortRoomCategoryFacilityGroupRepository resortRoomCategoryFacilityGroupRepository;

    public ResortRoomCategoryFacilityGroupServiceImpl(ResortRoomCategoryFacilityGroupRepository resortRoomCategoryFacilityGroupRepository) {
        this.resortRoomCategoryFacilityGroupRepository = resortRoomCategoryFacilityGroupRepository;
    }

    @Transactional
    @Override
    public SuccessResponse create(CreateResortRoomCategoryFacilityGroupRequest request,
                                  ResortRoomCategoryEntity resortRoomCategoryEntity,
                                  FacilityGroupEntity facilityGroupEntity,
                                  LocaleEntity localeEntity) {
        if (resortRoomCategoryFacilityGroupRepository.existsByResortRoomCategoryEntity_IdAndCodeAndIsActiveAndIsDeleted(
                resortRoomCategoryEntity.getId(), request.getCode(), true, false)) {
            throw new IllegalStateException("ResortRoomCategory already has a facility group with code: " + request.getCode());
        }

        if (facilityGroupEntity != null && resortRoomCategoryFacilityGroupRepository
                .existsByResortRoomCategoryEntity_IdAndFacilityGroupEntity_IdAndIsActiveAndIsDeleted(
                        resortRoomCategoryEntity.getId(), facilityGroupEntity.getId(), true, false)) {
            throw new IllegalStateException("ResortRoomCategory already has a facility group linked to platform facility group id: "
                    + facilityGroupEntity.getId());
        }

        ResortRoomCategoryFacilityGroupEntity entity = ResortRoomCategoryFacilityGroupMapper.create(request, facilityGroupEntity);
        resortRoomCategoryEntity.addResortRoomCategoryFacilityGroupEntity(entity);

        ResortRoomCategoryFacilityGroupLocaleEntity resortRoomCategoryFacilityGroupLocaleEntity = ResortRoomCategoryFacilityGroupLocaleMapper.create(request.getLocale());
        entity.addResortRoomCategoryFacilityGroupLocaleEntity(resortRoomCategoryFacilityGroupLocaleEntity);
        localeEntity.addResortRoomCategoryFacilityGroupLocaleEntity(resortRoomCategoryFacilityGroupLocaleEntity);

        resortRoomCategoryFacilityGroupRepository.save(entity);
        log.info("ResortRoomCategoryFacilityGroup created with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public ResortRoomCategoryFacilityGroupEntity getEntityById(Long resortRoomCategoryId, Long id) {
        return resortRoomCategoryFacilityGroupRepository
                .findByResortRoomCategoryEntity_IdAndIdAndIsActiveAndIsDeleted(resortRoomCategoryId, id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("ResortRoomCategoryFacilityGroup not found with id: " + id));
    }

    @Override
    public ResortRoomCategoryFacilityGroupResponse getById(Long resortRoomCategoryId, Long id) {
        ResortRoomCategoryFacilityGroupEntity entity = getEntityById(resortRoomCategoryId, id);
        ResortRoomCategoryFacilityGroupDto dto = ResortRoomCategoryFacilityGroupMapper.toDto(entity).build();
        return new ResortRoomCategoryFacilityGroupResponse(dto);
    }

    @Override
    public PaginatedResponse<ResortRoomCategoryFacilityGroupDto> getAll(Long resortRoomCategoryId, ResortRoomCategoryFacilityGroupFilterRequest request) {
        Specification<@NonNull ResortRoomCategoryFacilityGroupEntity> specification =
                ResortRoomCategoryFacilityGroupSpecification.filter(resortRoomCategoryId, request, LocaleContext.getLocaleId());
        Pageable pageable = request.toPageable(ALLOWED_SORT_FIELDS, ResortRoomCategoryFacilityGroupSortField.localeSortFields());
        Page<@NonNull ResortRoomCategoryFacilityGroupDto> page = resortRoomCategoryFacilityGroupRepository
                .findAll(specification, pageable)
                .map(entity -> ResortRoomCategoryFacilityGroupMapper.toDto(entity).build());
        return Pagination.buildPaginatedResponse(page, ALLOWED_SORT_FIELDS, ALLOWED_SEARCH_FIELDS);
    }

    @Transactional
    @Override
    public SuccessResponse update(ResortRoomCategoryFacilityGroupEntity entity, UpdateResortRoomCategoryFacilityGroupRequest request) {
        ResortRoomCategoryFacilityGroupMapper.update(entity, request);
        resortRoomCategoryFacilityGroupRepository.save(entity);
        log.info("ResortRoomCategoryFacilityGroup updated with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse delete(ResortRoomCategoryFacilityGroupEntity entity) {
        entity.setIsDeleted(true);
        entity.setIsActive(false);

        entity.getResortRoomCategoryFacilityGroupLocaleEntities().forEach(localeEntity -> {
            localeEntity.setIsDeleted(true);
            localeEntity.setIsActive(false);
        });

        resortRoomCategoryFacilityGroupRepository.save(entity);
        log.info("ResortRoomCategoryFacilityGroup soft-deleted with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }
}
