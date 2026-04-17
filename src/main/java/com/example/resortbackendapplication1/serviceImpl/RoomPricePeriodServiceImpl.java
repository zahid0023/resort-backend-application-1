package com.example.resortbackendapplication1.serviceImpl;

import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.dto.request.roompriceperiods.CreateRoomPricePeriodRequest;
import com.example.resortbackendapplication1.dto.request.roompriceperiods.UpdateRoomPricePeriodRequest;
import com.example.resortbackendapplication1.dto.response.roompriceperiods.RoomPricePeriodResponse;
import com.example.resortbackendapplication1.model.dto.RoomPricePeriodDto;
import com.example.resortbackendapplication1.model.entity.PriceTypeEntity;
import com.example.resortbackendapplication1.model.entity.RoomEntity;
import com.example.resortbackendapplication1.model.entity.RoomPricePeriodEntity;
import com.example.resortbackendapplication1.model.mapper.RoomPricePeriodMapper;
import com.example.resortbackendapplication1.repository.RoomPricePeriodRepository;
import com.example.resortbackendapplication1.service.PriceTypeService;
import com.example.resortbackendapplication1.service.RoomPricePeriodService;
import com.example.resortbackendapplication1.utils.Pagination;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RoomPricePeriodServiceImpl implements RoomPricePeriodService {

    private final RoomPricePeriodRepository roomPricePeriodRepository;
    private final PriceTypeService priceTypeService;

    public RoomPricePeriodServiceImpl(RoomPricePeriodRepository roomPricePeriodRepository, PriceTypeService priceTypeService) {
        this.roomPricePeriodRepository = roomPricePeriodRepository;
        this.priceTypeService = priceTypeService;
    }

    @Override
    public SuccessResponse createRoomPricePeriod(CreateRoomPricePeriodRequest request, RoomEntity roomEntity) {
        PriceTypeEntity priceTypeEntity = priceTypeService.getPriceTypeEntity(request.getPriceTypeId());
        RoomPricePeriodEntity entity = RoomPricePeriodMapper.fromRequest(request, roomEntity, priceTypeEntity);
        entity = roomPricePeriodRepository.save(entity);
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public RoomPricePeriodResponse getRoomPricePeriod(Long roomId, Long id) {
        RoomPricePeriodEntity entity = getRoomPricePeriodEntity(roomId, id);
        return new RoomPricePeriodResponse(RoomPricePeriodMapper.toDto(entity));
    }

    @Override
    public PaginatedResponse<RoomPricePeriodDto> getAllRoomPricePeriods(Long roomId, Pageable pageable) {
        Page<@NonNull RoomPricePeriodEntity> page = roomPricePeriodRepository
                .findAllByRoomEntity_IdAndIsActiveAndIsDeleted(roomId, true, false, pageable);
        Page<@NonNull RoomPricePeriodDto> dtoPage = page.map(RoomPricePeriodMapper::toDto);
        return Pagination.buildPaginatedResponse(dtoPage);
    }

    @Override
    public SuccessResponse updateRoomPricePeriod(Long roomId, Long id, UpdateRoomPricePeriodRequest request) {
        RoomPricePeriodEntity entity = getRoomPricePeriodEntity(roomId, id);
        PriceTypeEntity priceTypeEntity = request.getPriceTypeId() != null
                ? priceTypeService.getPriceTypeEntity(request.getPriceTypeId())
                : null;
        RoomPricePeriodMapper.updateEntity(entity, request, priceTypeEntity);
        entity = roomPricePeriodRepository.save(entity);
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public SuccessResponse deleteRoomPricePeriod(Long roomId, Long id) {
        RoomPricePeriodEntity entity = getRoomPricePeriodEntity(roomId, id);
        entity.setIsDeleted(true);
        entity.setIsActive(false);
        roomPricePeriodRepository.save(entity);
        return new SuccessResponse(true, id);
    }

    private RoomPricePeriodEntity getRoomPricePeriodEntity(Long roomId, Long id) {
        return roomPricePeriodRepository.findByRoomEntity_IdAndIdAndIsActiveAndIsDeleted(roomId, id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("Room Price Period with id: " + id + " was not found."));
    }
}
