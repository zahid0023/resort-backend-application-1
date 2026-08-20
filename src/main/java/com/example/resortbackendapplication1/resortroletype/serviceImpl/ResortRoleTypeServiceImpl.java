package com.example.resortbackendapplication1.resortroletype.serviceImpl;

import com.example.resortbackendapplication1.commons.context.LocaleContext;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.commons.utils.Pagination;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.resortroletype.dto.request.resortroletype.CreateResortRoleTypeRequest;
import com.example.resortbackendapplication1.resortroletype.dto.request.resortroletype.ResortRoleTypeFilterRequest;
import com.example.resortbackendapplication1.resortroletype.dto.request.resortroletype.UpdateResortRoleTypeRequest;
import com.example.resortbackendapplication1.resortroletype.dto.response.resortroletypes.ResortRoleTypeResponse;
import com.example.resortbackendapplication1.resortroletype.model.dto.ResortRoleTypeDto;
import com.example.resortbackendapplication1.resortroletype.model.entity.ResortRoleTypeEntity;
import com.example.resortbackendapplication1.resortroletype.model.entity.ResortRoleTypeLocaleEntity;
import com.example.resortbackendapplication1.resortroletype.model.enums.ResortRoleTypeSearchField;
import com.example.resortbackendapplication1.resortroletype.model.enums.ResortRoleTypeSortField;
import com.example.resortbackendapplication1.resortroletype.model.mapper.ResortRoleTypeLocaleMapper;
import com.example.resortbackendapplication1.resortroletype.model.mapper.ResortRoleTypeMapper;
import com.example.resortbackendapplication1.resortroletype.repository.ResortRoleTypeRepository;
import com.example.resortbackendapplication1.resortroletype.service.ResortRoleTypeService;
import com.example.resortbackendapplication1.resortroletype.specification.ResortRoleTypeSpecification;
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
public class ResortRoleTypeServiceImpl implements ResortRoleTypeService {

    private static final Set<String> ALLOWED_SORT_FIELDS = ResortRoleTypeSortField.allowedFields();
    private static final Set<String> ALLOWED_SEARCH_FIELDS = ResortRoleTypeSearchField.allowedFields();

    private final ResortRoleTypeRepository resortRoleTypeRepository;

    public ResortRoleTypeServiceImpl(ResortRoleTypeRepository resortRoleTypeRepository) {
        this.resortRoleTypeRepository = resortRoleTypeRepository;
    }

    @Transactional
    @Override
    public SuccessResponse create(CreateResortRoleTypeRequest request, LocaleEntity localeEntity) {
        if (resortRoleTypeRepository.existsByCodeAndIsActiveAndIsDeleted(request.getCode(), true, false)) {
            throw new IllegalStateException("ResortRoleType with code '" + request.getCode() + "' already exists");
        }

        ResortRoleTypeEntity entity = ResortRoleTypeMapper.create(request);

        ResortRoleTypeLocaleEntity resortRoleTypeLocaleEntity = ResortRoleTypeLocaleMapper.create(request.getLocale());
        localeEntity.addResortRoleTypeLocaleEntity(resortRoleTypeLocaleEntity);

        entity.addResortRoleTypeLocaleEntity(resortRoleTypeLocaleEntity);

        resortRoleTypeRepository.save(entity);
        log.info("ResortRoleType created with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public ResortRoleTypeEntity getEntityById(Long id) {
        return resortRoleTypeRepository.findByIdAndIsActiveAndIsDeleted(id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("ResortRoleType not found with id: " + id));
    }

    @Override
    public ResortRoleTypeEntity getEntityByCode(String code) {
        return resortRoleTypeRepository.findByCodeAndIsActiveAndIsDeleted(code, true, false)
                .orElseThrow(() -> new EntityNotFoundException("ResortRoleType not found with code: " + code));
    }

    @Override
    public ResortRoleTypeResponse getById(Long id) {
        ResortRoleTypeEntity entity = getEntityById(id);
        ResortRoleTypeDto dto = ResortRoleTypeMapper.toDto(entity).build();
        return new ResortRoleTypeResponse(dto);
    }

    @Override
    public PaginatedResponse<ResortRoleTypeDto> getAll(ResortRoleTypeFilterRequest request) {
        Specification<@NonNull ResortRoleTypeEntity> specification =
                ResortRoleTypeSpecification.filter(request, LocaleContext.getLocaleId());
        Pageable pageable = request.toPageable(ALLOWED_SORT_FIELDS, ResortRoleTypeSortField.localeSortFields());
        Page<@NonNull ResortRoleTypeDto> page = resortRoleTypeRepository
                .findAll(specification, pageable)
                .map(entity -> ResortRoleTypeMapper.toDto(entity).build());
        return Pagination.buildPaginatedResponse(page, ALLOWED_SORT_FIELDS, ALLOWED_SEARCH_FIELDS);
    }

    @Transactional
    @Override
    public SuccessResponse update(ResortRoleTypeEntity entity, UpdateResortRoleTypeRequest request) {
        ResortRoleTypeMapper.update(entity, request);
        resortRoleTypeRepository.save(entity);
        log.info("ResortRoleType updated with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse delete(ResortRoleTypeEntity entity) {
        entity.setIsDeleted(true);
        entity.setIsActive(false);

        entity.getResortRoleTypeLocaleEntities().forEach(localeEntity -> {
            localeEntity.setIsDeleted(true);
            localeEntity.setIsActive(false);
        });

        resortRoleTypeRepository.save(entity);
        log.info("ResortRoleType soft-deleted with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }
}
