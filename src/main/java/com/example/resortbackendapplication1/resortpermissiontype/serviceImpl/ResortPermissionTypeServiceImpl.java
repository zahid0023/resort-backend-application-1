package com.example.resortbackendapplication1.resortpermissiontype.serviceImpl;

import com.example.resortbackendapplication1.commons.context.LocaleContext;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.commons.utils.Pagination;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.resortpermissiontype.dto.request.resortpermissiontype.CreateResortPermissionTypeRequest;
import com.example.resortbackendapplication1.resortpermissiontype.dto.request.resortpermissiontype.ResortPermissionTypeFilterRequest;
import com.example.resortbackendapplication1.resortpermissiontype.dto.request.resortpermissiontype.UpdateResortPermissionTypeRequest;
import com.example.resortbackendapplication1.resortpermissiontype.dto.response.resortpermissiontypes.ResortPermissionTypeResponse;
import com.example.resortbackendapplication1.resortpermissiontype.model.dto.ResortPermissionTypeDto;
import com.example.resortbackendapplication1.resortpermissiontype.model.entity.ResortPermissionTypeEntity;
import com.example.resortbackendapplication1.resortpermissiontype.model.entity.ResortPermissionTypeLocaleEntity;
import com.example.resortbackendapplication1.resortpermissiontype.model.enums.ResortPermissionTypeSearchField;
import com.example.resortbackendapplication1.resortpermissiontype.model.enums.ResortPermissionTypeSortField;
import com.example.resortbackendapplication1.resortpermissiontype.model.mapper.ResortPermissionTypeLocaleMapper;
import com.example.resortbackendapplication1.resortpermissiontype.model.mapper.ResortPermissionTypeMapper;
import com.example.resortbackendapplication1.resortpermissiontype.repository.ResortPermissionTypeRepository;
import com.example.resortbackendapplication1.resortpermissiontype.service.ResortPermissionTypeService;
import com.example.resortbackendapplication1.resortpermissiontype.specification.ResortPermissionTypeSpecification;
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
public class ResortPermissionTypeServiceImpl implements ResortPermissionTypeService {

    private static final Set<String> ALLOWED_SORT_FIELDS = ResortPermissionTypeSortField.allowedFields();
    private static final Set<String> ALLOWED_SEARCH_FIELDS = ResortPermissionTypeSearchField.allowedFields();

    private final ResortPermissionTypeRepository resortPermissionTypeRepository;

    public ResortPermissionTypeServiceImpl(ResortPermissionTypeRepository resortPermissionTypeRepository) {
        this.resortPermissionTypeRepository = resortPermissionTypeRepository;
    }

    @Transactional
    @Override
    public SuccessResponse create(CreateResortPermissionTypeRequest request, LocaleEntity localeEntity) {
        if (resortPermissionTypeRepository.existsByCodeAndIsActiveAndIsDeleted(request.getCode(), true, false)) {
            throw new IllegalStateException("ResortPermissionType with code '" + request.getCode() + "' already exists");
        }

        ResortPermissionTypeEntity entity = ResortPermissionTypeMapper.create(request);

        ResortPermissionTypeLocaleEntity resortPermissionTypeLocaleEntity = ResortPermissionTypeLocaleMapper.create(request.getLocale());
        localeEntity.addResortPermissionTypeLocaleEntity(resortPermissionTypeLocaleEntity);

        entity.addResortPermissionTypeLocaleEntity(resortPermissionTypeLocaleEntity);

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
        ResortPermissionTypeDto dto = ResortPermissionTypeMapper.toDto(entity).build();
        return new ResortPermissionTypeResponse(dto);
    }

    @Override
    public PaginatedResponse<ResortPermissionTypeDto> getAll(ResortPermissionTypeFilterRequest request) {
        Specification<@NonNull ResortPermissionTypeEntity> specification =
                ResortPermissionTypeSpecification.filter(request, LocaleContext.getLocaleId());
        Pageable pageable = request.toPageable(ALLOWED_SORT_FIELDS, ResortPermissionTypeSortField.localeSortFields());
        Page<@NonNull ResortPermissionTypeDto> page = resortPermissionTypeRepository
                .findAll(specification, pageable)
                .map(entity -> ResortPermissionTypeMapper.toDto(entity).build());
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
    public SuccessResponse delete(ResortPermissionTypeEntity entity) {
        entity.setIsDeleted(true);
        entity.setIsActive(false);
        resortPermissionTypeRepository.save(entity);
        log.info("ResortPermissionType soft-deleted with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }
}
