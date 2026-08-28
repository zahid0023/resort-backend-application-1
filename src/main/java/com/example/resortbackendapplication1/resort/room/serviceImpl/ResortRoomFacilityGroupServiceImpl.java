package com.example.resortbackendapplication1.resort.room.serviceImpl;

import com.example.resortbackendapplication1.commons.context.LocaleContext;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.commons.utils.Pagination;
import com.example.resortbackendapplication1.facility.model.entity.FacilityGroupEntity;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.resort.room.dto.request.resortroomfacilitygroup.CreateResortRoomFacilityGroupRequest;
import com.example.resortbackendapplication1.resort.room.dto.request.resortroomfacilitygroup.ResortRoomFacilityGroupFilterRequest;
import com.example.resortbackendapplication1.resort.room.dto.request.resortroomfacilitygroup.UpdateResortRoomFacilityGroupRequest;
import com.example.resortbackendapplication1.resort.room.dto.response.resortroomfacilitygroups.ResortRoomFacilityGroupResponse;
import com.example.resortbackendapplication1.resort.room.model.dto.ResortRoomFacilityGroupDto;
import com.example.resortbackendapplication1.resort.room.model.entity.ResortRoomEntity;
import com.example.resortbackendapplication1.resort.room.model.entity.ResortRoomFacilityGroupEntity;
import com.example.resortbackendapplication1.resort.room.model.entity.ResortRoomFacilityGroupLocaleEntity;
import com.example.resortbackendapplication1.resort.room.model.enums.ResortRoomFacilityGroupSearchField;
import com.example.resortbackendapplication1.resort.room.model.enums.ResortRoomFacilityGroupSortField;
import com.example.resortbackendapplication1.resort.room.model.mapper.ResortRoomFacilityGroupLocaleMapper;
import com.example.resortbackendapplication1.resort.room.model.mapper.ResortRoomFacilityGroupMapper;
import com.example.resortbackendapplication1.resort.room.repository.ResortRoomFacilityGroupRepository;
import com.example.resortbackendapplication1.resort.room.service.ResortRoomFacilityGroupService;
import com.example.resortbackendapplication1.resort.room.specification.ResortRoomFacilityGroupSpecification;
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
public class ResortRoomFacilityGroupServiceImpl implements ResortRoomFacilityGroupService {

    private static final Set<String> ALLOWED_SORT_FIELDS = ResortRoomFacilityGroupSortField.allowedFields();
    private static final Set<String> ALLOWED_SEARCH_FIELDS = ResortRoomFacilityGroupSearchField.allowedFields();

    private final ResortRoomFacilityGroupRepository resortRoomFacilityGroupRepository;

    public ResortRoomFacilityGroupServiceImpl(ResortRoomFacilityGroupRepository resortRoomFacilityGroupRepository) {
        this.resortRoomFacilityGroupRepository = resortRoomFacilityGroupRepository;
    }

    @Transactional
    @Override
    public SuccessResponse create(CreateResortRoomFacilityGroupRequest request,
                                  ResortRoomEntity resortRoomEntity,
                                  FacilityGroupEntity facilityGroupEntity,
                                  LocaleEntity localeEntity) {
        if (resortRoomFacilityGroupRepository.existsByResortRoomEntity_IdAndCodeAndIsActiveAndIsDeleted(
                resortRoomEntity.getId(), request.getCode(), true, false)) {
            throw new IllegalStateException("ResortRoom already has a facility group with code: " + request.getCode());
        }

        if (facilityGroupEntity != null && resortRoomFacilityGroupRepository
                .existsByResortRoomEntity_IdAndFacilityGroupEntity_IdAndIsActiveAndIsDeleted(
                        resortRoomEntity.getId(), facilityGroupEntity.getId(), true, false)) {
            throw new IllegalStateException("ResortRoom already has a facility group linked to platform facility group id: "
                    + facilityGroupEntity.getId());
        }

        ResortRoomFacilityGroupEntity entity = ResortRoomFacilityGroupMapper.create(request, facilityGroupEntity);
        resortRoomEntity.addResortRoomFacilityGroupEntity(entity);

        ResortRoomFacilityGroupLocaleEntity resortRoomFacilityGroupLocaleEntity = ResortRoomFacilityGroupLocaleMapper.create(request.getLocale());
        entity.addResortRoomFacilityGroupLocaleEntity(resortRoomFacilityGroupLocaleEntity);
        localeEntity.addResortRoomFacilityGroupLocaleEntity(resortRoomFacilityGroupLocaleEntity);

        resortRoomFacilityGroupRepository.save(entity);
        log.info("ResortRoomFacilityGroup created with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public ResortRoomFacilityGroupEntity getEntityById(Long resortRoomId, Long id) {
        return resortRoomFacilityGroupRepository
                .findByResortRoomEntity_IdAndIdAndIsActiveAndIsDeleted(resortRoomId, id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("ResortRoomFacilityGroup not found with id: " + id));
    }

    @Override
    public ResortRoomFacilityGroupResponse getById(Long resortRoomId, Long id) {
        ResortRoomFacilityGroupEntity entity = getEntityById(resortRoomId, id);
        ResortRoomFacilityGroupDto dto = ResortRoomFacilityGroupMapper.toDto(entity).build();
        return new ResortRoomFacilityGroupResponse(dto);
    }

    @Override
    public PaginatedResponse<ResortRoomFacilityGroupDto> getAll(Long resortRoomId, ResortRoomFacilityGroupFilterRequest request) {
        Specification<@NonNull ResortRoomFacilityGroupEntity> specification =
                ResortRoomFacilityGroupSpecification.filter(resortRoomId, request, LocaleContext.getLocaleId());
        Pageable pageable = request.toPageable(ALLOWED_SORT_FIELDS, ResortRoomFacilityGroupSortField.localeSortFields());
        Page<@NonNull ResortRoomFacilityGroupDto> page = resortRoomFacilityGroupRepository
                .findAll(specification, pageable)
                .map(entity -> ResortRoomFacilityGroupMapper.toDto(entity).build());
        return Pagination.buildPaginatedResponse(page, ALLOWED_SORT_FIELDS, ALLOWED_SEARCH_FIELDS);
    }

    @Transactional
    @Override
    public SuccessResponse update(ResortRoomFacilityGroupEntity entity, UpdateResortRoomFacilityGroupRequest request) {
        ResortRoomFacilityGroupMapper.update(entity, request);
        resortRoomFacilityGroupRepository.save(entity);
        log.info("ResortRoomFacilityGroup updated with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse delete(ResortRoomFacilityGroupEntity entity) {
        entity.setIsDeleted(true);
        entity.setIsActive(false);

        entity.getResortRoomFacilityGroupLocaleEntities().forEach(localeEntity -> {
            localeEntity.setIsDeleted(true);
            localeEntity.setIsActive(false);
        });

        resortRoomFacilityGroupRepository.save(entity);
        log.info("ResortRoomFacilityGroup soft-deleted with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }
}
