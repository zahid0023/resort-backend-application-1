package com.example.resortbackendapplication1.resortpermissiontype.serviceImpl;

import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.commons.utils.EntityValidator;
import com.example.resortbackendapplication1.commons.utils.Pagination;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.resortpermissiontype.dto.request.CreateResortPermissionTypeRequest;
import com.example.resortbackendapplication1.resortpermissiontype.dto.request.ResortPermissionTypeFilterRequest;
import com.example.resortbackendapplication1.resortpermissiontype.dto.request.UpdateResortPermissionTypeRequest;
import com.example.resortbackendapplication1.resortpermissiontype.dto.response.ResortPermissionTypeResponse;
import com.example.resortbackendapplication1.resortpermissiontype.model.dto.ResortPermissionTypeDto;
import com.example.resortbackendapplication1.resortpermissiontype.model.entity.ResortPermissionTypeEntity;
import com.example.resortbackendapplication1.resortpermissiontype.model.enums.ResortPermissionTypeSearchField;
import com.example.resortbackendapplication1.resortpermissiontype.model.enums.ResortPermissionTypeSortField;
import com.example.resortbackendapplication1.resortpermissiontype.model.mapper.ResortPermissionTypeMapper;
import com.example.resortbackendapplication1.resortpermissiontype.repository.ResortPermissionTypeRepository;
import com.example.resortbackendapplication1.resortpermissiontype.service.ResortPermissionTypeService;
import com.example.resortbackendapplication1.resortpermissiontype.specification.ResortPermissionTypeSpecification;
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
public class ResortPermissionTypeServiceImpl implements ResortPermissionTypeService {

    private static final Set<String> ALLOWED_SORT_FIELDS = ResortPermissionTypeSortField.allowedFields();
    private static final Set<String> ALLOWED_SEARCH_FIELDS = ResortPermissionTypeSearchField.allowedFields();

    private final ResortPermissionTypeRepository resortPermissionTypeRepository;

    public ResortPermissionTypeServiceImpl(ResortPermissionTypeRepository resortPermissionTypeRepository) {
        this.resortPermissionTypeRepository = resortPermissionTypeRepository;
    }

    @Transactional
    @Override
    public SuccessResponse create(CreateResortPermissionTypeRequest request, Map<Long, LocaleEntity> localeEntityMap) {
        ResortPermissionTypeEntity entity = ResortPermissionTypeMapper.create(request, localeEntityMap);
        resortPermissionTypeRepository.save(entity);
        log.info("ResortPermissionType created with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public ResortPermissionTypeEntity getEntityById(Long id) {
        return resortPermissionTypeRepository.findByIdAndIsActiveAndIsDeleted(id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("ResortPermissionType not found with id: " + id));
    }

    @Override
    public ResortPermissionTypeResponse getById(Long id) {
        ResortPermissionTypeEntity entity = getEntityById(id);
        ResortPermissionTypeDto dto = ResortPermissionTypeMapper.toDto(entity);
        return new ResortPermissionTypeResponse(dto);
    }

    @Override
    public PaginatedResponse<ResortPermissionTypeDto> getAll(ResortPermissionTypeFilterRequest request) {
        Page<@NonNull ResortPermissionTypeDto> page = resortPermissionTypeRepository
                .findAll(ResortPermissionTypeSpecification.filter(request), request.toPageable(ALLOWED_SORT_FIELDS))
                .map(ResortPermissionTypeMapper::toDto);
        return Pagination.buildPaginatedResponse(page, ALLOWED_SORT_FIELDS, ALLOWED_SEARCH_FIELDS);
    }

    @Transactional
    @Override
    public SuccessResponse update(ResortPermissionTypeEntity entity, UpdateResortPermissionTypeRequest request) {
        ResortPermissionTypeMapper.update(entity, request);
        resortPermissionTypeRepository.save(entity);
        log.info("ResortPermissionType updated with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse delete(Long id) {
        ResortPermissionTypeEntity entity = getEntityById(id);
        entity.setIsDeleted(true);
        entity.setIsActive(false);
        resortPermissionTypeRepository.save(entity);
        log.info("ResortPermissionType soft-deleted with id: {}", id);
        return new SuccessResponse(true, id);
    }

    @Override
    public List<ResortPermissionTypeEntity> getAll(Set<Long> ids) {
        List<ResortPermissionTypeEntity> entities = resortPermissionTypeRepository
                .findAllByIdInAndIsActiveAndIsDeleted(ids, true, false);
        EntityValidator.validateAllFound(ids, entities, ResortPermissionTypeEntity::getId, "ResortPermissionType");
        return entities;
    }
}
