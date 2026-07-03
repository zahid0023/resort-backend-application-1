package com.example.resortbackendapplication1.uiblocksection.serviceImpl;

import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.commons.utils.EntityValidator;
import com.example.resortbackendapplication1.commons.utils.Pagination;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.uiblocksection.dto.request.uiblocksection.CreateUiBlockSectionRequest;
import com.example.resortbackendapplication1.uiblocksection.dto.request.uiblocksection.UiBlockSectionFilterRequest;
import com.example.resortbackendapplication1.uiblocksection.dto.request.uiblocksection.UpdateUiBlockSectionRequest;
import com.example.resortbackendapplication1.uiblocksection.dto.response.uiblocksections.UiBlockSectionResponse;
import com.example.resortbackendapplication1.uiblocksection.model.dto.UiBlockSectionDto;
import com.example.resortbackendapplication1.uiblocksection.model.entity.UiBlockSectionEntity;
import com.example.resortbackendapplication1.uiblocksection.model.enums.UiBlockSectionSearchField;
import com.example.resortbackendapplication1.uiblocksection.model.enums.UiBlockSectionSortField;
import com.example.resortbackendapplication1.uiblocksection.model.mapper.UiBlockSectionMapper;
import com.example.resortbackendapplication1.uiblocksection.repository.UiBlockSectionRepository;
import com.example.resortbackendapplication1.uiblocksection.service.UiBlockSectionService;
import com.example.resortbackendapplication1.uiblocksection.specification.UiBlockSectionSpecification;
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
public class UiBlockSectionServiceImpl implements UiBlockSectionService {

    private static final Set<String> ALLOWED_SORT_FIELDS = UiBlockSectionSortField.allowedFields();
    private static final Set<String> ALLOWED_SEARCH_FIELDS = UiBlockSectionSearchField.allowedFields();

    private final UiBlockSectionRepository uiBlockSectionRepository;

    public UiBlockSectionServiceImpl(UiBlockSectionRepository uiBlockSectionRepository) {
        this.uiBlockSectionRepository = uiBlockSectionRepository;
    }

    @Transactional
    @Override
    public SuccessResponse create(CreateUiBlockSectionRequest request, Map<Long, LocaleEntity> localeEntityMap) {
        UiBlockSectionEntity entity = UiBlockSectionMapper.create(request, localeEntityMap);
        uiBlockSectionRepository.save(entity);
        log.info("UiBlockSection created with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public UiBlockSectionResponse getById(Long id) {
        UiBlockSectionEntity entity = getEntityById(id);
        UiBlockSectionDto dto = UiBlockSectionMapper.toDto(entity);
        return new UiBlockSectionResponse(dto);
    }

    @Override
    public PaginatedResponse<UiBlockSectionDto> getAll(UiBlockSectionFilterRequest request) {
        Page<@NonNull UiBlockSectionDto> page = uiBlockSectionRepository
                .findAll(UiBlockSectionSpecification.filter(request), request.toPageable(ALLOWED_SORT_FIELDS))
                .map(UiBlockSectionMapper::toDto);
        return Pagination.buildPaginatedResponse(page, ALLOWED_SORT_FIELDS, ALLOWED_SEARCH_FIELDS);
    }

    @Transactional
    @Override
    public SuccessResponse update(UiBlockSectionEntity entity, UpdateUiBlockSectionRequest request) {
        UiBlockSectionMapper.update(entity, request);
        uiBlockSectionRepository.save(entity);
        log.info("UiBlockSection updated with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse delete(Long id) {
        UiBlockSectionEntity entity = getEntityById(id);
        entity.setIsDeleted(true);
        entity.setIsActive(false);
        uiBlockSectionRepository.save(entity);
        log.info("UiBlockSection soft-deleted with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public UiBlockSectionEntity getEntityById(Long id) {
        return uiBlockSectionRepository.findByIdAndIsActiveAndIsDeleted(id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("UiBlockSection not found with id: " + id));
    }

    @Override
    public List<UiBlockSectionEntity> getAll(Set<Long> ids) {
        List<UiBlockSectionEntity> entities = uiBlockSectionRepository.findAllByIdInAndIsActiveAndIsDeleted(ids, true, false);
        EntityValidator.validateAllFound(ids, entities, UiBlockSectionEntity::getId, "UiBlockSection");
        return entities;
    }
}
