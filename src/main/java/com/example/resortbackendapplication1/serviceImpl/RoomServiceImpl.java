package com.example.resortbackendapplication1.serviceImpl;

import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.dto.request.rooms.CreateRoomRequest;
import com.example.resortbackendapplication1.dto.request.rooms.UpdateRoomRequest;
import com.example.resortbackendapplication1.dto.response.rooms.RoomResponse;
import com.example.resortbackendapplication1.model.dto.RoomDto;
import com.example.resortbackendapplication1.model.entity.ResortRoomCategoryEntity;
import com.example.resortbackendapplication1.model.entity.RoomEntity;
import com.example.resortbackendapplication1.model.mapper.RoomMapper;
import com.example.resortbackendapplication1.repository.RoomRepository;
import com.example.resortbackendapplication1.service.RoomService;
import com.example.resortbackendapplication1.utils.Pagination;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;

    public RoomServiceImpl(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Override
    public SuccessResponse createRoom(CreateRoomRequest request, ResortRoomCategoryEntity resortRoomCategoryEntity) {
        RoomEntity entity = RoomMapper.fromRequest(request, resortRoomCategoryEntity);
        entity = roomRepository.save(entity);
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public RoomEntity getRoomEntity(Long resortRoomCategoryId, Long id) {
        return roomRepository.findByResortRoomCategoryEntity_IdAndIdAndIsActiveAndIsDeleted(resortRoomCategoryId, id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("Room with id: " + id + " was not found."));
    }

    @Override
    public RoomResponse getRoom(Long resortRoomCategoryId, Long id) {
        RoomEntity entity = getRoomEntity(resortRoomCategoryId, id);
        return new RoomResponse(RoomMapper.toDto(entity));
    }

    @Override
    public PaginatedResponse<RoomDto> getAllRooms(Long resortRoomCategoryId, Pageable pageable) {
        Page<@NonNull RoomEntity> page = roomRepository
                .findAllByResortRoomCategoryEntity_IdAndIsActiveAndIsDeleted(resortRoomCategoryId, true, false, pageable);
        Page<@NonNull RoomDto> dtoPage = page.map(RoomMapper::toDto);
        return Pagination.buildPaginatedResponse(dtoPage);
    }

    @Override
    public SuccessResponse updateRoom(RoomEntity entity, UpdateRoomRequest request) {
        RoomMapper.updateEntity(entity, request);
        entity = roomRepository.save(entity);
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public SuccessResponse deleteRoom(Long resortRoomCategoryId, Long id) {
        RoomEntity entity = getRoomEntity(resortRoomCategoryId, id);
        entity.setIsDeleted(true);
        entity.setIsActive(false);
        roomRepository.save(entity);
        return new SuccessResponse(true, id);
    }
}
