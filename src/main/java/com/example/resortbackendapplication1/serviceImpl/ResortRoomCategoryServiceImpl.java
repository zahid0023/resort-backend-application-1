package com.example.resortbackendapplication1.serviceImpl;

import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.dto.request.resortroomcategories.CreateResortRoomCategoryRequest;
import com.example.resortbackendapplication1.dto.request.resortroomcategories.UpdateResortRoomCategoryRequest;
import com.example.resortbackendapplication1.dto.response.resortroomcategories.ResortRoomCategoryResponse;
import com.example.resortbackendapplication1.model.dto.ResortRoomCategoryDto;
import com.example.resortbackendapplication1.model.entity.ResortEntity;
import com.example.resortbackendapplication1.model.entity.ResortRoomCategoryEntity;
import com.example.resortbackendapplication1.model.entity.RoomCategoryEntity;
import com.example.resortbackendapplication1.model.mapper.ResortRoomCategoryMapper;
import com.example.resortbackendapplication1.repository.ResortRoomCategoryRepository;
import com.example.resortbackendapplication1.service.ResortRoomCategoryService;
import com.example.resortbackendapplication1.utils.Pagination;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ResortRoomCategoryServiceImpl implements ResortRoomCategoryService {

    private final ResortRoomCategoryRepository resortRoomCategoryRepository;

    public ResortRoomCategoryServiceImpl(ResortRoomCategoryRepository resortRoomCategoryRepository) {
        this.resortRoomCategoryRepository = resortRoomCategoryRepository;
    }

    @Override
    public SuccessResponse createResortRoomCategory(CreateResortRoomCategoryRequest request,
                                                    ResortEntity resortEntity,
                                                    RoomCategoryEntity roomCategoryEntity) {
        ResortRoomCategoryEntity entity = ResortRoomCategoryMapper.fromRequest(request, resortEntity, roomCategoryEntity);
        entity = resortRoomCategoryRepository.save(entity);
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public ResortRoomCategoryEntity getResortRoomCategoryEntity(Long resortId, Long id) {
        return resortRoomCategoryRepository.findByResort_IdAndIdAndIsActiveAndIsDeleted(resortId, id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("Resort Room Category with id: " + id + " was not found."));
    }

    @Override
    public ResortRoomCategoryResponse getResortRoomCategory(Long resortId, Long id) {
        ResortRoomCategoryEntity entity = getResortRoomCategoryEntity(resortId, id);
        return new ResortRoomCategoryResponse(ResortRoomCategoryMapper.toDto(entity));
    }

    @Override
    public PaginatedResponse<ResortRoomCategoryDto> getAllResortRoomCategories(Long resortId, Pageable pageable) {
        Page<@NonNull ResortRoomCategoryEntity> page = resortRoomCategoryRepository
                .findAllByResort_IdAndIsActiveAndIsDeleted(resortId, true, false, pageable);
        Page<@NonNull ResortRoomCategoryDto> dtoPage = page.map(ResortRoomCategoryMapper::toDto);
        return Pagination.buildPaginatedResponse(dtoPage);
    }

    @Override
    public SuccessResponse updateResortRoomCategory(ResortRoomCategoryEntity entity, UpdateResortRoomCategoryRequest request) {
        ResortRoomCategoryMapper.updateEntity(entity, request);
        entity = resortRoomCategoryRepository.save(entity);
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public SuccessResponse deleteResortRoomCategory(Long resortId, Long id) {
        ResortRoomCategoryEntity entity = getResortRoomCategoryEntity(resortId, id);
        entity.setIsDeleted(true);
        entity.setIsActive(false);
        resortRoomCategoryRepository.save(entity);
        return new SuccessResponse(true, id);
    }
}
