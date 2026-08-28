package com.example.resortbackendapplication1.resort.roomcategory.serviceImpl;

import com.example.resortbackendapplication1.commons.context.LocaleContext;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.commons.utils.Pagination;
import com.example.resortbackendapplication1.facility.model.entity.FacilityEntity;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.resort.roomcategory.dto.request.resortroomcategoryfacility.CreateResortRoomCategoryFacilityRequest;
import com.example.resortbackendapplication1.resort.roomcategory.dto.request.resortroomcategoryfacility.ResortRoomCategoryFacilityFilterRequest;
import com.example.resortbackendapplication1.resort.roomcategory.dto.request.resortroomcategoryfacility.UpdateResortRoomCategoryFacilityRequest;
import com.example.resortbackendapplication1.resort.roomcategory.dto.response.resortroomcategoryfacilities.ResortRoomCategoryFacilityResponse;
import com.example.resortbackendapplication1.resort.roomcategory.model.dto.ResortRoomCategoryFacilityDto;
import com.example.resortbackendapplication1.resort.roomcategory.model.entity.ResortRoomCategoryEntity;
import com.example.resortbackendapplication1.resort.roomcategory.model.entity.ResortRoomCategoryFacilityEntity;
import com.example.resortbackendapplication1.resort.roomcategory.model.entity.ResortRoomCategoryFacilityGroupEntity;
import com.example.resortbackendapplication1.resort.roomcategory.model.entity.ResortRoomCategoryFacilityLocaleEntity;
import com.example.resortbackendapplication1.resort.roomcategory.model.enums.ResortRoomCategoryFacilitySearchField;
import com.example.resortbackendapplication1.resort.roomcategory.model.enums.ResortRoomCategoryFacilitySortField;
import com.example.resortbackendapplication1.resort.roomcategory.model.mapper.ResortRoomCategoryFacilityLocaleMapper;
import com.example.resortbackendapplication1.resort.roomcategory.model.mapper.ResortRoomCategoryFacilityMapper;
import com.example.resortbackendapplication1.resort.roomcategory.repository.ResortRoomCategoryFacilityRepository;
import com.example.resortbackendapplication1.resort.roomcategory.service.ResortRoomCategoryFacilityService;
import com.example.resortbackendapplication1.resort.roomcategory.specification.ResortRoomCategoryFacilitySpecification;
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
public class ResortRoomCategoryFacilityServiceImpl implements ResortRoomCategoryFacilityService {

    private static final Set<String> ALLOWED_SORT_FIELDS = ResortRoomCategoryFacilitySortField.allowedFields();
    private static final Set<String> ALLOWED_SEARCH_FIELDS = ResortRoomCategoryFacilitySearchField.allowedFields();

    private final ResortRoomCategoryFacilityRepository resortRoomCategoryFacilityRepository;

    public ResortRoomCategoryFacilityServiceImpl(ResortRoomCategoryFacilityRepository resortRoomCategoryFacilityRepository) {
        this.resortRoomCategoryFacilityRepository = resortRoomCategoryFacilityRepository;
    }

    @Transactional
    @Override
    public SuccessResponse create(CreateResortRoomCategoryFacilityRequest request,
                                  ResortRoomCategoryEntity resortRoomCategoryEntity,
                                  ResortRoomCategoryFacilityGroupEntity resortRoomCategoryFacilityGroupEntity,
                                  FacilityEntity facilityEntity,
                                  LocaleEntity localeEntity) {
        if (resortRoomCategoryFacilityRepository.existsByResortRoomCategoryEntity_IdAndCodeAndIsActiveAndIsDeleted(
                resortRoomCategoryEntity.getId(), request.getCode(), true, false)) {
            throw new IllegalStateException("ResortRoomCategory already has a facility with code: " + request.getCode());
        }

        if (facilityEntity != null && resortRoomCategoryFacilityRepository
                .existsByResortRoomCategoryEntity_IdAndFacilityEntity_IdAndIsActiveAndIsDeleted(
                        resortRoomCategoryEntity.getId(), facilityEntity.getId(), true, false)) {
            throw new IllegalStateException("ResortRoomCategory already has a facility linked to platform facility id: "
                    + facilityEntity.getId());
        }

        ResortRoomCategoryFacilityEntity entity = ResortRoomCategoryFacilityMapper.create(request, facilityEntity);
        resortRoomCategoryEntity.addResortRoomCategoryFacilityEntity(entity);
        resortRoomCategoryFacilityGroupEntity.addResortRoomCategoryFacilityEntity(entity);

        ResortRoomCategoryFacilityLocaleEntity resortRoomCategoryFacilityLocaleEntity = ResortRoomCategoryFacilityLocaleMapper.create(request.getLocale());
        entity.addResortRoomCategoryFacilityLocaleEntity(resortRoomCategoryFacilityLocaleEntity);
        localeEntity.addResortRoomCategoryFacilityLocaleEntity(resortRoomCategoryFacilityLocaleEntity);

        resortRoomCategoryFacilityRepository.save(entity);
        log.info("ResortRoomCategoryFacility created with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public ResortRoomCategoryFacilityEntity getEntityById(Long resortRoomCategoryId, Long id) {
        return resortRoomCategoryFacilityRepository
                .findByResortRoomCategoryEntity_IdAndIdAndIsActiveAndIsDeleted(resortRoomCategoryId, id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("ResortRoomCategoryFacility not found with id: " + id));
    }

    @Override
    public ResortRoomCategoryFacilityResponse getById(Long resortRoomCategoryId, Long id) {
        ResortRoomCategoryFacilityEntity entity = getEntityById(resortRoomCategoryId, id);
        ResortRoomCategoryFacilityDto dto = ResortRoomCategoryFacilityMapper.toDto(entity).build();
        return new ResortRoomCategoryFacilityResponse(dto);
    }

    @Override
    public PaginatedResponse<ResortRoomCategoryFacilityDto> getAll(Long resortRoomCategoryId, ResortRoomCategoryFacilityFilterRequest request) {
        Specification<@NonNull ResortRoomCategoryFacilityEntity> specification =
                ResortRoomCategoryFacilitySpecification.filter(resortRoomCategoryId, request, LocaleContext.getLocaleId());
        Pageable pageable = request.toPageable(ALLOWED_SORT_FIELDS, ResortRoomCategoryFacilitySortField.localeSortFields());
        Page<@NonNull ResortRoomCategoryFacilityDto> page = resortRoomCategoryFacilityRepository
                .findAll(specification, pageable)
                .map(entity -> ResortRoomCategoryFacilityMapper.toDto(entity).build());
        return Pagination.buildPaginatedResponse(page, ALLOWED_SORT_FIELDS, ALLOWED_SEARCH_FIELDS);
    }

    @Transactional
    @Override
    public SuccessResponse update(ResortRoomCategoryFacilityEntity entity,
                                  UpdateResortRoomCategoryFacilityRequest request) {
        ResortRoomCategoryFacilityMapper.update(entity, request);
        resortRoomCategoryFacilityRepository.save(entity);
        log.info("ResortRoomCategoryFacility updated with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse delete(ResortRoomCategoryFacilityEntity entity) {
        entity.setIsDeleted(true);
        entity.setIsActive(false);

        entity.getResortRoomCategoryFacilityLocaleEntities().forEach(localeEntity -> {
            localeEntity.setIsDeleted(true);
            localeEntity.setIsActive(false);
        });

        resortRoomCategoryFacilityRepository.save(entity);
        log.info("ResortRoomCategoryFacility soft-deleted with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }
}
