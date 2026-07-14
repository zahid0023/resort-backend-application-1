package com.example.resortbackendapplication1.resortaccesstype.serviceImpl;

import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.commons.utils.EntityValidator;
import com.example.resortbackendapplication1.commons.utils.Pagination;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.resortaccesstype.dto.request.CreateResortAccessTypeRequest;
import com.example.resortbackendapplication1.resortaccesstype.dto.request.ResortAccessTypeFilterRequest;
import com.example.resortbackendapplication1.resortaccesstype.dto.request.UpdateResortAccessTypeRequest;
import com.example.resortbackendapplication1.resortaccesstype.dto.response.ResortAccessTypeResponse;
import com.example.resortbackendapplication1.resortaccesstype.model.dto.ResortAccessTypeDto;
import com.example.resortbackendapplication1.resortaccesstype.model.entity.ResortAccessTypeEntity;
import com.example.resortbackendapplication1.resortaccesstype.model.enums.ResortAccessTypeSearchField;
import com.example.resortbackendapplication1.resortaccesstype.model.enums.ResortAccessTypeSortField;
import com.example.resortbackendapplication1.resortaccesstype.model.mapper.ResortAccessTypeMapper;
import com.example.resortbackendapplication1.resortaccesstype.repository.ResortAccessTypeRepository;
import com.example.resortbackendapplication1.resortaccesstype.service.ResortAccessTypeService;
import com.example.resortbackendapplication1.resortaccesstype.specification.ResortAccessTypeSpecification;
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
public class ResortAccessTypeServiceImpl implements ResortAccessTypeService {

    private static final Set<String> ALLOWED_SORT_FIELDS = ResortAccessTypeSortField.allowedFields();
    private static final Set<String> ALLOWED_SEARCH_FIELDS = ResortAccessTypeSearchField.allowedFields();

    private final ResortAccessTypeRepository resortAccessTypeRepository;

    public ResortAccessTypeServiceImpl(ResortAccessTypeRepository resortAccessTypeRepository) {
        this.resortAccessTypeRepository = resortAccessTypeRepository;
    }

    @Transactional
    @Override
    public SuccessResponse create(CreateResortAccessTypeRequest request, Map<Long, LocaleEntity> localeEntityMap) {
        ResortAccessTypeEntity entity = ResortAccessTypeMapper.create(request, localeEntityMap);
        resortAccessTypeRepository.save(entity);
        log.info("ResortAccessType created with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public ResortAccessTypeEntity getEntityById(Long id) {
        return resortAccessTypeRepository.findByIdAndIsActiveAndIsDeleted(id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("ResortAccessType not found with id: " + id));
    }

    @Override
    public ResortAccessTypeEntity getEntityByCode(String code) {
        return resortAccessTypeRepository.findByCodeAndIsActiveAndIsDeleted(code, true, false)
                .orElseThrow(() -> new EntityNotFoundException("ResortAccessType not found with code: " + code));
    }

    @Override
    public ResortAccessTypeResponse getById(Long id) {
        ResortAccessTypeEntity entity = getEntityById(id);
        ResortAccessTypeDto dto = ResortAccessTypeMapper.toDto(entity);
        return new ResortAccessTypeResponse(dto);
    }

    @Override
    public PaginatedResponse<ResortAccessTypeDto> getAll(ResortAccessTypeFilterRequest request) {
        Page<@NonNull ResortAccessTypeDto> page = resortAccessTypeRepository
                .findAll(ResortAccessTypeSpecification.filter(request), request.toPageable(ALLOWED_SORT_FIELDS))
                .map(ResortAccessTypeMapper::toDto);
        return Pagination.buildPaginatedResponse(page, ALLOWED_SORT_FIELDS, ALLOWED_SEARCH_FIELDS);
    }

    @Transactional
    @Override
    public SuccessResponse update(ResortAccessTypeEntity entity, UpdateResortAccessTypeRequest request) {
        ResortAccessTypeMapper.update(entity, request);
        resortAccessTypeRepository.save(entity);
        log.info("ResortAccessType updated with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse delete(Long id) {
        ResortAccessTypeEntity entity = getEntityById(id);
        entity.setIsDeleted(true);
        entity.setIsActive(false);
        resortAccessTypeRepository.save(entity);
        log.info("ResortAccessType soft-deleted with id: {}", id);
        return new SuccessResponse(true, id);
    }

    @Override
    public List<ResortAccessTypeEntity> getAll(Set<Long> ids) {
        List<ResortAccessTypeEntity> entities = resortAccessTypeRepository
                .findAllByIdInAndIsActiveAndIsDeleted(ids, true, false);
        EntityValidator.validateAllFound(ids, entities, ResortAccessTypeEntity::getId, "ResortAccessType");
        return entities;
    }
}
