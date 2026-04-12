package com.example.resortapplication1.serviceImpl;

import com.example.resortapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortapplication1.commons.dto.response.SuccessResponse;
import com.example.resortapplication1.dto.request.roomcategories.CreateRoomCategoryRequest;
import com.example.resortapplication1.dto.request.roomcategories.UpdateRoomCategoryRequest;
import com.example.resortapplication1.dto.response.roomcategories.RoomCategoryResponse;
import com.example.resortapplication1.model.dto.RoomCategoryDto;
import com.example.resortapplication1.model.entity.RoomCategoryEntity;
import com.example.resortapplication1.model.mapper.RoomCategoryMapper;
import com.example.resortapplication1.repository.RoomCategoryRepository;
import com.example.resortapplication1.service.RoomCategoryService;
import com.example.resortapplication1.utils.Pagination;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RoomCategoryServiceImpl implements RoomCategoryService {

    private final RoomCategoryRepository roomCategoryRepository;

    public RoomCategoryServiceImpl(RoomCategoryRepository roomCategoryRepository) {
        this.roomCategoryRepository = roomCategoryRepository;
    }

    @Override
    public SuccessResponse createRoomCategory(CreateRoomCategoryRequest request) {
        RoomCategoryEntity entity = RoomCategoryMapper.fromRequest(request);
        entity = roomCategoryRepository.save(entity);
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public RoomCategoryEntity getRoomCategoryEntity(Long id) {
        return roomCategoryRepository.findByIdAndIsActiveAndIsDeleted(id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("Room Category with id: " + id + " was not found."));
    }

    @Override
    public RoomCategoryResponse getRoomCategory(Long id) {
        RoomCategoryEntity entity = getRoomCategoryEntity(id);
        RoomCategoryDto dto = RoomCategoryMapper.toDto(entity);
        return new RoomCategoryResponse(dto);
    }

    @Override
    public PaginatedResponse<RoomCategoryDto> getAllRoomCategories(Pageable pageable) {
        Page<@NonNull RoomCategoryEntity> page = roomCategoryRepository.findAllByIsActiveAndIsDeleted(true, false, pageable);
        Page<@NonNull RoomCategoryDto> dtoPage = page.map(RoomCategoryMapper::toDto);
        return Pagination.buildPaginatedResponse(dtoPage);
    }

    @Override
    public SuccessResponse updateRoomCategory(Long id, UpdateRoomCategoryRequest request) {
        RoomCategoryEntity entity = getRoomCategoryEntity(id);
        RoomCategoryMapper.updateEntity(entity, request);
        entity = roomCategoryRepository.save(entity);
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public SuccessResponse deleteRoomCategory(Long id) {
        RoomCategoryEntity entity = getRoomCategoryEntity(id);
        entity.setIsDeleted(true);
        entity.setIsActive(false);
        roomCategoryRepository.save(entity);
        return new SuccessResponse(true, id);
    }
}
