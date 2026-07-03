package com.example.resortbackendapplication1.pagetype.serviceImpl;

import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.commons.utils.EntityValidator;
import com.example.resortbackendapplication1.commons.utils.Pagination;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.pagetype.dto.request.pagetype.CreatePageTypeRequest;
import com.example.resortbackendapplication1.pagetype.dto.request.pagetype.PageTypeFilterRequest;
import com.example.resortbackendapplication1.pagetype.dto.request.pagetype.UpdatePageTypeRequest;
import com.example.resortbackendapplication1.pagetype.dto.response.pagetypes.PageTypeResponse;
import com.example.resortbackendapplication1.pagetype.model.dto.PageTypeDto;
import com.example.resortbackendapplication1.pagetype.model.entity.PageTypeEntity;
import com.example.resortbackendapplication1.pagetype.model.enums.PageTypeSearchField;
import com.example.resortbackendapplication1.pagetype.model.enums.PageTypeSortField;
import com.example.resortbackendapplication1.pagetype.model.mapper.PageTypeMapper;
import com.example.resortbackendapplication1.pagetype.repository.PageTypeRepository;
import com.example.resortbackendapplication1.pagetype.service.PageTypeService;
import com.example.resortbackendapplication1.pagetype.specification.PageTypeSpecification;
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
public class PageTypeServiceImpl implements PageTypeService {

    private static final Set<String> ALLOWED_SORT_FIELDS = PageTypeSortField.allowedFields();
    private static final Set<String> ALLOWED_SEARCH_FIELDS = PageTypeSearchField.allowedFields();

    private final PageTypeRepository pageTypeRepository;

    public PageTypeServiceImpl(PageTypeRepository pageTypeRepository) {
        this.pageTypeRepository = pageTypeRepository;
    }

    @Transactional
    @Override
    public SuccessResponse create(CreatePageTypeRequest request, Map<Long, LocaleEntity> localeEntityMap) {
        PageTypeEntity entity = PageTypeMapper.create(request, localeEntityMap);
        pageTypeRepository.save(entity);
        log.info("PageType created with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public PageTypeResponse getById(Long id) {
        PageTypeEntity entity = getEntityById(id);
        PageTypeDto dto = PageTypeMapper.toDto(entity);
        return new PageTypeResponse(dto);
    }

    @Override
    public PaginatedResponse<PageTypeDto> getAll(PageTypeFilterRequest request) {
        Page<@NonNull PageTypeDto> page = pageTypeRepository
                .findAll(PageTypeSpecification.filter(request), request.toPageable(ALLOWED_SORT_FIELDS))
                .map(PageTypeMapper::toDto);
        return Pagination.buildPaginatedResponse(page, ALLOWED_SORT_FIELDS, ALLOWED_SEARCH_FIELDS);
    }

    @Transactional
    @Override
    public SuccessResponse update(PageTypeEntity entity, UpdatePageTypeRequest request) {
        PageTypeMapper.update(entity, request);
        pageTypeRepository.save(entity);
        log.info("PageType updated with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse delete(Long id) {
        PageTypeEntity entity = getEntityById(id);
        entity.setIsDeleted(true);
        entity.setIsActive(false);
        pageTypeRepository.save(entity);
        log.info("PageType soft-deleted with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public PageTypeEntity getEntityById(Long id) {
        return pageTypeRepository.findByIdAndIsActiveAndIsDeleted(id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("PageType not found with id: " + id));
    }

    @Override
    public List<PageTypeEntity> getAll(Set<Long> ids) {
        List<PageTypeEntity> entities = pageTypeRepository.findAllByIdInAndIsActiveAndIsDeleted(ids, true, false);
        EntityValidator.validateAllFound(ids, entities, PageTypeEntity::getId, "PageType");
        return entities;
    }
}
