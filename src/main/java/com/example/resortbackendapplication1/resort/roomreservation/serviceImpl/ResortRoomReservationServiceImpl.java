package com.example.resortbackendapplication1.resort.roomreservation.serviceImpl;

import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.utils.Pagination;
import com.example.resortbackendapplication1.currency.model.entity.CurrencyEntity;
import com.example.resortbackendapplication1.currency.model.mapper.CurrencyMapper;
import com.example.resortbackendapplication1.price.model.mapper.PriceUnitMapper;
import com.example.resortbackendapplication1.reservation.model.entity.ReservationStatusEntity;
import com.example.resortbackendapplication1.reservation.model.mapper.ReservationStatusMapper;
import com.example.resortbackendapplication1.resort.booking.model.entity.ResortBookingEntity;
import com.example.resortbackendapplication1.resort.pricing.RoomPricingResolver;
import com.example.resortbackendapplication1.resort.room.model.entity.ResortRoomEntity;
import com.example.resortbackendapplication1.resort.roomreservation.dto.request.roomreservation.CreateResortRoomReservationRequest;
import com.example.resortbackendapplication1.resort.roomreservation.dto.request.roomreservation.ResortRoomReservationFilterRequest;
import com.example.resortbackendapplication1.resort.roomreservation.model.dto.ResortRoomReservationDto;
import com.example.resortbackendapplication1.resort.roomreservation.model.entity.ResortRoomReservationEntity;
import com.example.resortbackendapplication1.resort.roomreservation.model.enums.ResortRoomReservationSortField;
import com.example.resortbackendapplication1.resort.roomreservation.model.mapper.ResortRoomReservationMapper;
import com.example.resortbackendapplication1.resort.roomreservation.repository.ResortRoomReservationRepository;
import com.example.resortbackendapplication1.resort.roomreservation.service.ResortRoomReservationService;
import com.example.resortbackendapplication1.resort.room.model.mapper.ResortRoomMapper;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class ResortRoomReservationServiceImpl implements ResortRoomReservationService {

    private static final Set<String> ALLOWED_SORT_FIELDS = ResortRoomReservationSortField.allowedFields();

    private final ResortRoomReservationRepository resortRoomReservationRepository;
    private final RoomPricingResolver roomPricingResolver;

    public ResortRoomReservationServiceImpl(ResortRoomReservationRepository resortRoomReservationRepository,
                                            RoomPricingResolver roomPricingResolver) {
        this.resortRoomReservationRepository = resortRoomReservationRepository;
        this.roomPricingResolver = roomPricingResolver;
    }

    @Override
    public List<ResortRoomReservationEntity> initialize(List<CreateResortRoomReservationRequest> request,
                                                        ResortBookingEntity resortBookingEntity,
                                                        Map<Long, ResortRoomEntity> resortRoomEntityMap,
                                                        Map<Long, ReservationStatusEntity> reservationStatusEntityMap,
                                                        CurrencyEntity currencyEntity) {
        return request.stream()
                .map(roomRequest -> buildAndAttach(roomRequest, resortBookingEntity, resortRoomEntityMap,
                        reservationStatusEntityMap, currencyEntity))
                .toList();
    }

    private ResortRoomReservationEntity buildAndAttach(CreateResortRoomReservationRequest roomRequest,
                                                       ResortBookingEntity resortBookingEntity,
                                                       Map<Long, ResortRoomEntity> resortRoomEntityMap,
                                                       Map<Long, ReservationStatusEntity> reservationStatusEntityMap,
                                                       CurrencyEntity currencyEntity) {
        ResortRoomEntity resortRoomEntity = resortRoomEntityMap.get(roomRequest.getResortRoomId());
        ReservationStatusEntity reservationStatusEntity = reservationStatusEntityMap.get(roomRequest.getReservationStatusId());
        Long resortRoomCategoryId = resortRoomEntity.getResortRoomCategoryEntity().getId();

        RoomPricingResolver.NightlyResult pricing = roomPricingResolver.resolveNightly(
                resortRoomEntity.getResortRoomCategoryEntity().getResortEntity(), resortRoomEntity, resortRoomCategoryId,
                currencyEntity.getId(), roomRequest.getCheckIn(), roomRequest.getCheckOut());

        ResortRoomReservationEntity entity = ResortRoomReservationMapper.create(roomRequest, reservationStatusEntity,
                currencyEntity, pricing.priceUnitEntity(), pricing.nights());
        resortRoomEntity.addResortRoomReservationEntity(entity);
        resortBookingEntity.addResortRoomReservationEntity(entity);
        return entity;
    }

    @Override
    public PaginatedResponse<ResortRoomReservationDto> getAll(Long resortId, ResortRoomReservationFilterRequest request) {
        Pageable pageable = request.toPageable(ALLOWED_SORT_FIELDS);
        Page<@NonNull ResortRoomReservationDto> page = resortRoomReservationRepository
                .findByResortRoomEntity_ResortRoomCategoryEntity_ResortEntity_IdAndIsActiveAndIsDeleted(resortId, true, false, pageable)
                .map(this::toFullDto);
        return Pagination.buildPaginatedResponse(page, ALLOWED_SORT_FIELDS, Set.of());
    }

    private ResortRoomReservationDto toFullDto(ResortRoomReservationEntity entity) {
        return ResortRoomReservationMapper.toDto(entity)
                .resortRoom(ResortRoomMapper.toDto(entity.getResortRoomEntity()).build())
                .reservationStatus(ReservationStatusMapper.toDto(entity.getReservationStatusEntity()).build())
                .currency(CurrencyMapper.toDto(entity.getCurrencyEntity()).build())
                .priceUnit(PriceUnitMapper.toDto(entity.getPriceUnitEntity()).build())
                .build();
    }
}
