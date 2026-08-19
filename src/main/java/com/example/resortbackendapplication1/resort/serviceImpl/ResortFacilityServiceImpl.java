package com.example.resortbackendapplication1.resort.serviceImpl;

import com.example.resortbackendapplication1.commons.context.LocaleContext;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.commons.utils.Pagination;
import com.example.resortbackendapplication1.facility.model.entity.FacilityEntity;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.resort.dto.request.resortfacility.CreateResortFacilityRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortfacility.ResortFacilityFilterRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortfacility.UpdateResortFacilityRequest;
import com.example.resortbackendapplication1.resort.dto.response.resortfacilities.ResortFacilityResponse;
import com.example.resortbackendapplication1.resort.model.dto.ResortFacilityDto;
import com.example.resortbackendapplication1.resort.model.entity.ResortEntity;
import com.example.resortbackendapplication1.resort.model.entity.ResortFacilityEntity;
import com.example.resortbackendapplication1.resort.model.entity.ResortFacilityGroupEntity;
import com.example.resortbackendapplication1.resort.model.entity.ResortFacilityLocaleEntity;
import com.example.resortbackendapplication1.resort.model.enums.ResortFacilitySearchField;
import com.example.resortbackendapplication1.resort.model.enums.ResortFacilitySortField;
import com.example.resortbackendapplication1.resort.model.mapper.ResortFacilityLocaleMapper;
import com.example.resortbackendapplication1.resort.model.mapper.ResortFacilityMapper;
import com.example.resortbackendapplication1.resort.repository.ResortFacilityRepository;
import com.example.resortbackendapplication1.resort.service.ResortFacilityService;
import com.example.resortbackendapplication1.resort.specification.ResortFacilitySpecification;
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
public class ResortFacilityServiceImpl implements ResortFacilityService {

    private static final Set<String> ALLOWED_SORT_FIELDS = ResortFacilitySortField.allowedFields();
    private static final Set<String> ALLOWED_SEARCH_FIELDS = ResortFacilitySearchField.allowedFields();

    private final ResortFacilityRepository resortFacilityRepository;

    public ResortFacilityServiceImpl(ResortFacilityRepository resortFacilityRepository) {
        this.resortFacilityRepository = resortFacilityRepository;
    }

    @Transactional
    @Override
    public SuccessResponse create(CreateResortFacilityRequest request,
                                  ResortEntity resortEntity,
                                  ResortFacilityGroupEntity resortFacilityGroupEntity,
                                  FacilityEntity facilityEntity,
                                  LocaleEntity localeEntity) {
        if (facilityEntity != null && resortFacilityRepository
                .existsByResortEntity_IdAndFacilityEntity_IdAndIsActiveAndIsDeleted(
                        resortEntity.getId(), facilityEntity.getId(), true, false)) {
            throw new IllegalStateException("Resort already has a facility linked to platform facility id: "
                    + facilityEntity.getId());
        }

        ResortFacilityEntity entity = ResortFacilityMapper.create(request, facilityEntity);
        resortEntity.addResortFacilityEntity(entity);
        resortFacilityGroupEntity.addResortFacilityEntity(entity);

        ResortFacilityLocaleEntity resortFacilityLocaleEntity = ResortFacilityLocaleMapper.create(request.getLocale());
        entity.addResortFacilityLocaleEntity(resortFacilityLocaleEntity);
        localeEntity.addResortFacilityLocaleEntity(resortFacilityLocaleEntity);

        resortFacilityRepository.save(entity);
        log.info("ResortFacility created with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public ResortFacilityEntity getEntityById(Long resortId, Long id) {
        return resortFacilityRepository.findByResortEntity_IdAndIdAndIsActiveAndIsDeleted(resortId, id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("ResortFacility not found with id: " + id));
    }

    @Override
    public ResortFacilityResponse getById(Long resortId, Long id) {
        ResortFacilityEntity entity = getEntityById(resortId, id);
        ResortFacilityDto dto = ResortFacilityMapper.toDto(entity).build();
        return new ResortFacilityResponse(dto);
    }

    @Override
    public PaginatedResponse<ResortFacilityDto> getAll(Long resortId, ResortFacilityFilterRequest request) {
        Specification<@NonNull ResortFacilityEntity> specification =
                ResortFacilitySpecification.filter(resortId, request, LocaleContext.getLocaleId());
        Pageable pageable = request.toPageable(ALLOWED_SORT_FIELDS, ResortFacilitySortField.localeSortFields());
        Page<@NonNull ResortFacilityDto> page = resortFacilityRepository
                .findAll(specification, pageable)
                .map(entity -> ResortFacilityMapper.toDto(entity).build());
        return Pagination.buildPaginatedResponse(page, ALLOWED_SORT_FIELDS, ALLOWED_SEARCH_FIELDS);
    }

    @Transactional
    @Override
    public SuccessResponse update(ResortFacilityEntity entity,
                                  ResortFacilityGroupEntity resortFacilityGroupEntity,
                                  UpdateResortFacilityRequest request) {
        ResortFacilityGroupEntity currentGroup = entity.getResortFacilityGroupEntity();
        if (!currentGroup.getId().equals(resortFacilityGroupEntity.getId())) {
            currentGroup.removeResortFacilityEntity(entity);
            resortFacilityGroupEntity.addResortFacilityEntity(entity);
        }

        ResortFacilityMapper.update(entity, request);
        resortFacilityRepository.save(entity);
        log.info("ResortFacility updated with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse delete(ResortFacilityEntity entity) {
        entity.setIsDeleted(true);
        entity.setIsActive(false);
        resortFacilityRepository.save(entity);
        log.info("ResortFacility soft-deleted with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }
}
