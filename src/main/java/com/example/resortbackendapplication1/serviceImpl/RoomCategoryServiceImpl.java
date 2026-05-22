package com.example.resortbackendapplication1.serviceImpl;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.dto.request.roomcategories.CreateRoomCategoryRequest;
import com.example.resortbackendapplication1.dto.request.roomcategories.UpdateRoomCategoryRequest;
import com.example.resortbackendapplication1.dto.response.roomcategories.RoomCategoryResponse;
import com.example.resortbackendapplication1.model.dto.RoomCategoryDto;
import com.example.resortbackendapplication1.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.model.entity.RoomCategoryEntity;
import com.example.resortbackendapplication1.model.enums.RoomCategorySortField;
import com.example.resortbackendapplication1.model.mapper.RoomCategoryMapper;
import com.example.resortbackendapplication1.model.projection.RoomCategorySummary;
import com.example.resortbackendapplication1.repository.RoomCategoryRepository;
import com.example.resortbackendapplication1.service.RoomCategoryService;
import com.example.resortbackendapplication1.utils.Pagination;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Set;

@Service
@Slf4j
public class RoomCategoryServiceImpl implements RoomCategoryService {

    private static final Set<String> ALLOWED_SORT_FIELDS = RoomCategorySortField.allowedFields();

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
    public PaginatedResponse<RoomCategorySummary> getAll(PaginatedRequest request) {
        Page<@NonNull RoomCategorySummary> page = roomCategoryRepository.findAllByIsActiveAndIsDeleted(
                true, false, request.toPageable(ALLOWED_SORT_FIELDS)
        );
        return Pagination.buildPaginatedResponse(page);
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
        log.info("RoomCategory soft-deleted with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }
}
