package com.example.resortbackendapplication1.roomcategory.serviceImpl;

import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.commons.utils.EntityValidator;
import com.example.resortbackendapplication1.commons.utils.Pagination;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.roomcategory.dto.request.roomcategory.CreateRoomCategoryRequest;
import com.example.resortbackendapplication1.roomcategory.dto.request.roomcategory.RoomCategoryFilterRequest;
import com.example.resortbackendapplication1.roomcategory.dto.request.roomcategory.UpdateRoomCategoryRequest;
import com.example.resortbackendapplication1.roomcategory.dto.response.roomcategories.RoomCategoryResponse;
import com.example.resortbackendapplication1.roomcategory.model.dto.RoomCategoryDto;
import com.example.resortbackendapplication1.roomcategory.model.entity.RoomCategoryEntity;
import com.example.resortbackendapplication1.roomcategory.model.enums.RoomCategorySearchField;
import com.example.resortbackendapplication1.roomcategory.model.enums.RoomCategorySortField;
import com.example.resortbackendapplication1.roomcategory.model.mapper.RoomCategoryMapper;
import com.example.resortbackendapplication1.roomcategory.repository.RoomCategoryRepository;
import com.example.resortbackendapplication1.roomcategory.service.RoomCategoryService;
import com.example.resortbackendapplication1.roomcategory.specification.RoomCategorySpecification;
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
public class RoomCategoryServiceImpl implements RoomCategoryService {

    private static final Set<String> ALLOWED_SORT_FIELDS = RoomCategorySortField.allowedFields();
    private static final Set<String> ALLOWED_SEARCH_FIELDS = RoomCategorySearchField.allowedFields();

    private final RoomCategoryRepository roomCategoryRepository;

    public RoomCategoryServiceImpl(RoomCategoryRepository roomCategoryRepository) {
        this.roomCategoryRepository = roomCategoryRepository;
    }

    @Transactional
    @Override
    public SuccessResponse create(CreateRoomCategoryRequest request, Map<Long, LocaleEntity> localeEntityMap) {
        RoomCategoryEntity entity = RoomCategoryMapper.create(request, localeEntityMap);
        roomCategoryRepository.save(entity);
        log.info("RoomCategory created with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public RoomCategoryEntity getEntityById(Long id) {
        return roomCategoryRepository.findByIdAndIsActiveAndIsDeleted(id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("RoomCategory not found with id: " + id));
    }

    @Override
    public RoomCategoryResponse getById(Long id) {
        RoomCategoryEntity entity = getEntityById(id);
        RoomCategoryDto dto = RoomCategoryMapper.toDto(entity);
        return new RoomCategoryResponse(dto);
    }

    @Override
    public PaginatedResponse<RoomCategoryDto> getAll(RoomCategoryFilterRequest request) {
        Page<@NonNull RoomCategoryDto> page = roomCategoryRepository
                .findAll(RoomCategorySpecification.filter(request), request.toPageable(ALLOWED_SORT_FIELDS))
                .map(RoomCategoryMapper::toDto);
        return Pagination.buildPaginatedResponse(page, ALLOWED_SORT_FIELDS, ALLOWED_SEARCH_FIELDS);
    }

    @Transactional
    @Override
    public SuccessResponse update(RoomCategoryEntity entity, UpdateRoomCategoryRequest request) {
        RoomCategoryMapper.update(entity, request);
        roomCategoryRepository.save(entity);
        log.info("RoomCategory updated with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse delete(Long id) {
        RoomCategoryEntity entity = getEntityById(id);
        entity.setIsDeleted(true);
        entity.setIsActive(false);
        roomCategoryRepository.save(entity);
        log.info("RoomCategory soft-deleted with id: {}", id);
        return new SuccessResponse(true, id);
    }

    @Override
    public List<RoomCategoryEntity> getAll(Set<Long> ids) {
        List<RoomCategoryEntity> entities = roomCategoryRepository.findAllByIdInAndIsActiveAndIsDeleted(ids, true, false);
        EntityValidator.validateAllFound(ids, entities, RoomCategoryEntity::getId, "RoomCategory");
        return entities;
    }
}
