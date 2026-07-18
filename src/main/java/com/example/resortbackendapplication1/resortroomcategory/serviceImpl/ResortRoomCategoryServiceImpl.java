package com.example.resortbackendapplication1.resortroomcategory.serviceImpl;

import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.commons.utils.Pagination;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.resort.model.entity.ResortEntity;
import com.example.resortbackendapplication1.resortroomcategory.dto.request.CreateResortRoomCategoryRequest;
import com.example.resortbackendapplication1.resortroomcategory.dto.request.ResortRoomCategoryFilterRequest;
import com.example.resortbackendapplication1.resortroomcategory.dto.request.UpdateResortRoomCategoryRequest;
import com.example.resortbackendapplication1.resortroomcategory.dto.response.ResortRoomCategoryResponse;
import com.example.resortbackendapplication1.resortroomcategory.model.dto.ResortRoomCategoryDto;
import com.example.resortbackendapplication1.resortroomcategory.model.entity.ResortRoomCategoryEntity;
import com.example.resortbackendapplication1.resortroomcategory.model.enums.ResortRoomCategorySearchField;
import com.example.resortbackendapplication1.resortroomcategory.model.enums.ResortRoomCategorySortField;
import com.example.resortbackendapplication1.resortroomcategory.model.mapper.ResortRoomCategoryMapper;
import com.example.resortbackendapplication1.resortroomcategory.repository.ResortRoomCategoryRepository;
import com.example.resortbackendapplication1.resortroomcategory.service.ResortRoomCategoryService;
import com.example.resortbackendapplication1.resortroomcategory.specification.ResortRoomCategorySpecification;
import com.example.resortbackendapplication1.roomcategory.model.entity.RoomCategoryEntity;
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
public class ResortRoomCategoryServiceImpl implements ResortRoomCategoryService {

    private static final Set<String> ALLOWED_SORT_FIELDS = ResortRoomCategorySortField.allowedFields();
    private static final Set<String> ALLOWED_SEARCH_FIELDS = ResortRoomCategorySearchField.allowedFields();

    private final ResortRoomCategoryRepository resortRoomCategoryRepository;

    public ResortRoomCategoryServiceImpl(ResortRoomCategoryRepository resortRoomCategoryRepository) {
        this.resortRoomCategoryRepository = resortRoomCategoryRepository;
    }

    @Transactional
    @Override
    public SuccessResponse create(CreateResortRoomCategoryRequest request,
                                   ResortEntity resortEntity,
                                   RoomCategoryEntity roomCategoryEntity,
                                   Map<Long, LocaleEntity> localeEntityMap) {
        ResortRoomCategoryEntity entity = ResortRoomCategoryMapper.create(request, resortEntity, roomCategoryEntity, localeEntityMap);
        resortRoomCategoryRepository.save(entity);
        log.info("ResortRoomCategory created with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public ResortRoomCategoryEntity getEntityById(Long resortId, Long id) {
        return resortRoomCategoryRepository
                .findByIdAndResortEntity_IdAndIsActiveAndIsDeleted(id, resortId, true, false)
                .orElseThrow(() -> new EntityNotFoundException("ResortRoomCategory not found with id: " + id));
    }

    @Override
    public ResortRoomCategoryResponse getById(Long resortId, Long id) {
        ResortRoomCategoryEntity entity = getEntityById(resortId, id);
        return new ResortRoomCategoryResponse(ResortRoomCategoryMapper.toDto(entity));
    }

    @Override
    public PaginatedResponse<ResortRoomCategoryDto> getAll(ResortRoomCategoryFilterRequest request, Long resortId) {
        Page<@NonNull ResortRoomCategoryDto> page = resortRoomCategoryRepository
                .findAll(ResortRoomCategorySpecification.filter(request, resortId), request.toPageable(ALLOWED_SORT_FIELDS))
                .map(ResortRoomCategoryMapper::toDto);
        return Pagination.buildPaginatedResponse(page, ALLOWED_SORT_FIELDS, ALLOWED_SEARCH_FIELDS);
    }

    @Transactional
    @Override
    public SuccessResponse update(ResortRoomCategoryEntity entity, UpdateResortRoomCategoryRequest request) {
        ResortRoomCategoryMapper.update(entity, request);
        resortRoomCategoryRepository.save(entity);
        log.info("ResortRoomCategory updated with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse delete(ResortRoomCategoryEntity entity) {
        entity.setIsDeleted(true);
        entity.setIsActive(false);
        resortRoomCategoryRepository.save(entity);
        log.info("ResortRoomCategory soft-deleted with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }
}
