package com.example.resortbackendapplication1.resort.serviceImpl;

import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.commons.utils.EntityValidator;
import com.example.resortbackendapplication1.commons.utils.Pagination;
import com.example.resortbackendapplication1.facility.model.entity.FacilityEntity;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.resort.dto.request.resortfacility.CreateResortFacilityRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortfacility.ResortFacilityFilterRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortfacility.UpdateResortFacilityRequest;
import com.example.resortbackendapplication1.resort.dto.response.ResortFacilityResponse;
import com.example.resortbackendapplication1.resort.model.dto.ResortFacilityDto;
import com.example.resortbackendapplication1.resort.model.entity.ResortEntity;
import com.example.resortbackendapplication1.resort.model.entity.ResortFacilityEntity;
import com.example.resortbackendapplication1.resort.model.entity.ResortFacilityGroupEntity;
import com.example.resortbackendapplication1.resort.model.enums.ResortFacilitySortField;
import com.example.resortbackendapplication1.resort.model.mapper.ResortFacilityMapper;
import com.example.resortbackendapplication1.resort.repository.ResortFacilityRepository;
import com.example.resortbackendapplication1.resort.service.ResortFacilityService;
import com.example.resortbackendapplication1.resort.specification.ResortFacilitySpecification;
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
public class ResortFacilityServiceImpl implements ResortFacilityService {

    private static final Set<String> ALLOWED_SORT_FIELDS = ResortFacilitySortField.allowedFields();
    private static final Set<String> ALLOWED_SEARCH_FIELDS = Set.of();

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
                                  Map<Long, LocaleEntity> localeEntityMap) {
        ResortFacilityEntity entity = ResortFacilityMapper.create(request, resortEntity, resortFacilityGroupEntity, facilityEntity, localeEntityMap);
        resortFacilityRepository.save(entity);
        log.info("ResortFacility created with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public ResortFacilityEntity getEntityById(Long id) {
        return resortFacilityRepository.findByIdAndIsActiveAndIsDeleted(id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("ResortFacility not found with id: " + id));
    }

    @Override
    public ResortFacilityResponse getById(Long id) {
        ResortFacilityEntity entity = getEntityById(id);
        return new ResortFacilityResponse(ResortFacilityMapper.toDto(entity));
    }

    @Override
    public PaginatedResponse<ResortFacilityDto> getAll(ResortFacilityFilterRequest request, Long resortFacilityGroupId) {
        Page<@NonNull ResortFacilityDto> page = resortFacilityRepository
                .findAll(ResortFacilitySpecification.filter(request, resortFacilityGroupId), request.toPageable(ALLOWED_SORT_FIELDS))
                .map(ResortFacilityMapper::toDto);
        return Pagination.buildPaginatedResponse(page, ALLOWED_SORT_FIELDS, ALLOWED_SEARCH_FIELDS);
    }

    @Transactional
    @Override
    public SuccessResponse update(ResortFacilityEntity entity,
                                  UpdateResortFacilityRequest request,
                                  FacilityEntity facilityEntity) {
        ResortFacilityMapper.update(entity, request, facilityEntity);
        resortFacilityRepository.save(entity);
        log.info("ResortFacility updated with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse delete(Long id) {
        ResortFacilityEntity entity = getEntityById(id);
        entity.setIsDeleted(true);
        entity.setIsActive(false);
        resortFacilityRepository.save(entity);
        log.info("ResortFacility soft-deleted with id: {}", id);
        return new SuccessResponse(true, id);
    }

    @Override
    public List<ResortFacilityEntity> getAll(Set<Long> ids) {
        List<ResortFacilityEntity> entities = resortFacilityRepository
                .findAllByIdInAndIsActiveAndIsDeleted(ids, true, false);
        EntityValidator.validateAllFound(ids, entities, ResortFacilityEntity::getId, "ResortFacility");
        return entities;
    }
}
