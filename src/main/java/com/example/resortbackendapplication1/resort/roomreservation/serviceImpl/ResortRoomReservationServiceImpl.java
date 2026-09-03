package com.example.resortbackendapplication1.resort.roomreservation.serviceImpl;

import com.example.resortbackendapplication1.currency.model.entity.CurrencyEntity;
import com.example.resortbackendapplication1.currency.model.mapper.CurrencyMapper;
import com.example.resortbackendapplication1.price.model.mapper.PriceUnitMapper;
import com.example.resortbackendapplication1.reservation.model.entity.ReservationStatusEntity;
import com.example.resortbackendapplication1.reservation.model.mapper.ReservationStatusMapper;
import com.example.resortbackendapplication1.resort.booking.model.entity.ResortBookingEntity;
import com.example.resortbackendapplication1.resort.pricing.RoomPricingResolver;
import com.example.resortbackendapplication1.resort.room.model.entity.ResortRoomEntity;
import com.example.resortbackendapplication1.resort.roomreservation.dto.request.roomreservation.CreateResortRoomReservationRequest;
import com.example.resortbackendapplication1.resort.roomreservation.model.dto.ResortRoomReservationDto;
import com.example.resortbackendapplication1.resort.roomreservation.model.entity.ResortRoomReservationEntity;
import com.example.resortbackendapplication1.resort.roomreservation.model.mapper.ResortRoomReservationMapper;
import com.example.resortbackendapplication1.resort.roomreservation.repository.ResortRoomReservationRepository;
import com.example.resortbackendapplication1.resort.roomreservation.service.ResortRoomReservationService;
import com.example.resortbackendapplication1.resort.room.model.mapper.ResortRoomMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ResortRoomReservationServiceImpl implements ResortRoomReservationService {

    private final ResortRoomReservationRepository resortRoomReservationRepository;
    private final RoomPricingResolver roomPricingResolver;

    public ResortRoomReservationServiceImpl(ResortRoomReservationRepository resortRoomReservationRepository,
                                            RoomPricingResolver roomPricingResolver) {
        this.resortRoomReservationRepository = resortRoomReservationRepository;
        this.roomPricingResolver = roomPricingResolver;
    }

    @Override
    public void attachReservationEntities(List<CreateResortRoomReservationRequest> request,
                                   ResortBookingEntity resortBookingEntity,
                                   Map<Long, ResortRoomEntity> resortRoomEntityMap,
                                   Map<Long, ReservationStatusEntity> reservationStatusEntityMap,
                                   CurrencyEntity currencyEntity) {
        request.forEach(roomRequest -> buildAndAttach(roomRequest, resortBookingEntity, resortRoomEntityMap,
                reservationStatusEntityMap, currencyEntity));
    }

    private void buildAndAttach(CreateResortRoomReservationRequest roomRequest,
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

        ResortRoomReservationEntity entity = ResortRoomReservationMapper.create(roomRequest);
        entity.setReservationStatusEntity(reservationStatusEntity);
        entity.setCurrencyEntity(currencyEntity);
        entity.setPriceUnitEntity(pricing.priceUnitEntity());
        ResortRoomReservationMapper.applyPricing(entity, pricing.nights());
        resortRoomEntity.addResortRoomReservationEntity(entity);

        resortBookingEntity.addResortRoomReservationEntity(entity);
    }

    @Override
    public ResortRoomReservationDto toDto(ResortRoomReservationEntity entity) {
        return ResortRoomReservationMapper.toDto(entity)
                .resortRoom(ResortRoomMapper.toDto(entity.getResortRoomEntity()).build())
                .reservationStatus(ReservationStatusMapper.toDto(entity.getReservationStatusEntity()).build())
                .currency(CurrencyMapper.toDto(entity.getCurrencyEntity()).build())
                .priceUnit(PriceUnitMapper.toDto(entity.getPriceUnitEntity()).build())
                .build();
    }
}
