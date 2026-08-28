package com.example.resortbackendapplication1.resort.room.serviceImpl;

import com.example.resortbackendapplication1.commons.context.LocaleContext;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.commons.utils.Pagination;
import com.example.resortbackendapplication1.facility.model.entity.FacilityEntity;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.resort.room.dto.request.resortroomfacility.CreateResortRoomFacilityRequest;
import com.example.resortbackendapplication1.resort.room.dto.request.resortroomfacility.ResortRoomFacilityFilterRequest;
import com.example.resortbackendapplication1.resort.room.dto.request.resortroomfacility.UpdateResortRoomFacilityRequest;
import com.example.resortbackendapplication1.resort.room.dto.response.resortroomfacilities.ResortRoomFacilityResponse;
import com.example.resortbackendapplication1.resort.room.model.dto.ResortRoomFacilityDto;
import com.example.resortbackendapplication1.resort.room.model.entity.ResortRoomEntity;
import com.example.resortbackendapplication1.resort.room.model.entity.ResortRoomFacilityEntity;
import com.example.resortbackendapplication1.resort.room.model.entity.ResortRoomFacilityGroupEntity;
import com.example.resortbackendapplication1.resort.room.model.entity.ResortRoomFacilityLocaleEntity;
import com.example.resortbackendapplication1.resort.room.model.enums.ResortRoomFacilitySearchField;
import com.example.resortbackendapplication1.resort.room.model.enums.ResortRoomFacilitySortField;
import com.example.resortbackendapplication1.resort.room.model.mapper.ResortRoomFacilityLocaleMapper;
import com.example.resortbackendapplication1.resort.room.model.mapper.ResortRoomFacilityMapper;
import com.example.resortbackendapplication1.resort.room.repository.ResortRoomFacilityRepository;
import com.example.resortbackendapplication1.resort.room.service.ResortRoomFacilityService;
import com.example.resortbackendapplication1.resort.room.specification.ResortRoomFacilitySpecification;
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
public class ResortRoomFacilityServiceImpl implements ResortRoomFacilityService {

    private static final Set<String> ALLOWED_SORT_FIELDS = ResortRoomFacilitySortField.allowedFields();
    private static final Set<String> ALLOWED_SEARCH_FIELDS = ResortRoomFacilitySearchField.allowedFields();

    private final ResortRoomFacilityRepository resortRoomFacilityRepository;

    public ResortRoomFacilityServiceImpl(ResortRoomFacilityRepository resortRoomFacilityRepository) {
        this.resortRoomFacilityRepository = resortRoomFacilityRepository;
    }

    @Transactional
    @Override
    public SuccessResponse create(CreateResortRoomFacilityRequest request,
                                  ResortRoomEntity resortRoomEntity,
                                  ResortRoomFacilityGroupEntity resortRoomFacilityGroupEntity,
                                  FacilityEntity facilityEntity,
                                  LocaleEntity localeEntity) {
        if (resortRoomFacilityRepository.existsByResortRoomEntity_IdAndCodeAndIsActiveAndIsDeleted(
                resortRoomEntity.getId(), request.getCode(), true, false)) {
            throw new IllegalStateException("ResortRoom already has a facility with code: " + request.getCode());
        }

        if (facilityEntity != null && resortRoomFacilityRepository
                .existsByResortRoomEntity_IdAndFacilityEntity_IdAndIsActiveAndIsDeleted(
                        resortRoomEntity.getId(), facilityEntity.getId(), true, false)) {
            throw new IllegalStateException("ResortRoom already has a facility linked to platform facility id: "
                    + facilityEntity.getId());
        }

        ResortRoomFacilityEntity entity = ResortRoomFacilityMapper.create(request, facilityEntity);
        resortRoomEntity.addResortRoomFacilityEntity(entity);
        resortRoomFacilityGroupEntity.addResortRoomFacilityEntity(entity);

        ResortRoomFacilityLocaleEntity resortRoomFacilityLocaleEntity = ResortRoomFacilityLocaleMapper.create(request.getLocale());
        entity.addResortRoomFacilityLocaleEntity(resortRoomFacilityLocaleEntity);
        localeEntity.addResortRoomFacilityLocaleEntity(resortRoomFacilityLocaleEntity);

        resortRoomFacilityRepository.save(entity);
        log.info("ResortRoomFacility created with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public ResortRoomFacilityEntity getEntityById(Long resortRoomId, Long id) {
        return resortRoomFacilityRepository
                .findByResortRoomEntity_IdAndIdAndIsActiveAndIsDeleted(resortRoomId, id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("ResortRoomFacility not found with id: " + id));
    }

    @Override
    public ResortRoomFacilityResponse getById(Long resortRoomId, Long id) {
        ResortRoomFacilityEntity entity = getEntityById(resortRoomId, id);
        ResortRoomFacilityDto dto = ResortRoomFacilityMapper.toDto(entity).build();
        return new ResortRoomFacilityResponse(dto);
    }

    @Override
    public PaginatedResponse<ResortRoomFacilityDto> getAll(Long resortRoomId, ResortRoomFacilityFilterRequest request) {
        Specification<@NonNull ResortRoomFacilityEntity> specification =
                ResortRoomFacilitySpecification.filter(resortRoomId, request, LocaleContext.getLocaleId());
        Pageable pageable = request.toPageable(ALLOWED_SORT_FIELDS, ResortRoomFacilitySortField.localeSortFields());
        Page<@NonNull ResortRoomFacilityDto> page = resortRoomFacilityRepository
                .findAll(specification, pageable)
                .map(entity -> ResortRoomFacilityMapper.toDto(entity).build());
        return Pagination.buildPaginatedResponse(page, ALLOWED_SORT_FIELDS, ALLOWED_SEARCH_FIELDS);
    }

    @Transactional
    @Override
    public SuccessResponse update(ResortRoomFacilityEntity entity,
                                  UpdateResortRoomFacilityRequest request) {
        ResortRoomFacilityMapper.update(entity, request);
        resortRoomFacilityRepository.save(entity);
        log.info("ResortRoomFacility updated with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse delete(ResortRoomFacilityEntity entity) {
        entity.setIsDeleted(true);
        entity.setIsActive(false);

        entity.getResortRoomFacilityLocaleEntities().forEach(localeEntity -> {
            localeEntity.setIsDeleted(true);
            localeEntity.setIsActive(false);
        });

        resortRoomFacilityRepository.save(entity);
        log.info("ResortRoomFacility soft-deleted with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }
}
