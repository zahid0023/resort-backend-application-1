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
import com.example.resortbackendapplication1.facility.model.entity.FacilityEntity;
import com.example.resortbackendapplication1.facility.model.entity.FacilityGroupEntity;
import com.example.resortbackendapplication1.facility.model.entity.FacilityLocaleEntity;
import com.example.resortbackendapplication1.facility.model.enums.FacilitySearchField;
import com.example.resortbackendapplication1.facility.model.enums.FacilitySortField;
import com.example.resortbackendapplication1.facility.model.mapper.FacilityGroupMapper;
import com.example.resortbackendapplication1.facility.model.mapper.FacilityLocaleMapper;
import com.example.resortbackendapplication1.facility.model.mapper.FacilityMapper;
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

import java.util.Set;

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
                                  FacilityGroupEntity facilityGroupEntity,
                                  LocaleEntity localeEntity) {
        if (facilityRepository.existsByCodeAndIsActiveAndIsDeleted(request.getCode(), true, false)) {
            throw new IllegalStateException("Facility with code '" + request.getCode() + "' already exists");
        }

        FacilityEntity entity = FacilityMapper.create(request);
        facilityGroupEntity.addFacilityEntity(entity);

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
        FacilityGroupDto facilityGroup = FacilityGroupMapper.toDto(entity.getFacilityGroupEntity()).build();
        FacilityDto dto = FacilityMapper.toDto(entity)
                .facilityGroup(facilityGroup)
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
                        .facilityGroup(FacilityGroupMapper.toDto(entity.getFacilityGroupEntity()).build())
                        .build());
        return Pagination.buildPaginatedResponse(page, ALLOWED_SORT_FIELDS, ALLOWED_SEARCH_FIELDS);
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
        facilityRepository.save(entity);
        log.info("Facility soft-deleted with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }
}
