package com.example.resortbackendapplication1.resort.booking.serviceImpl;

import com.example.resortbackendapplication1.auth.model.entity.UserEntity;
import com.example.resortbackendapplication1.bookingsource.model.entity.BookingSourceEntity;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.currency.model.entity.CurrencyEntity;
import com.example.resortbackendapplication1.reservation.model.entity.ReservationStatusEntity;
import com.example.resortbackendapplication1.resort.booking.dto.request.booking.CreateResortBookingRequest;
import com.example.resortbackendapplication1.resort.booking.model.entity.ResortBookingEntity;
import com.example.resortbackendapplication1.resort.booking.model.mapper.ResortBookingMapper;
import com.example.resortbackendapplication1.resort.booking.repository.ResortBookingRepository;
import com.example.resortbackendapplication1.resort.booking.service.ResortBookingService;
import com.example.resortbackendapplication1.resort.core.model.entity.ResortEntity;
import com.example.resortbackendapplication1.resort.room.model.entity.ResortRoomEntity;
import com.example.resortbackendapplication1.resort.roomreservation.dto.request.roomreservation.CreateResortRoomReservationRequest;
import com.example.resortbackendapplication1.resort.roomreservation.service.ResortRoomReservationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Injects {@code ResortRoomReservationService} directly — a deliberate, named exception to the usual
 * controller-orchestrates-cross-domain rule, since a reservation's whole purpose is to serve availability and
 * booking (see {@code ResortBookingService#createPosBooking}'s javadoc).
 */
@Service
@Slf4j
public class ResortBookingServiceImpl implements ResortBookingService {

    private final ResortBookingRepository resortBookingRepository;
    private final ResortRoomReservationService resortRoomReservationService;

    public ResortBookingServiceImpl(ResortBookingRepository resortBookingRepository,
                                    ResortRoomReservationService resortRoomReservationService) {
        this.resortBookingRepository = resortBookingRepository;
        this.resortRoomReservationService = resortRoomReservationService;
    }

    @Transactional
    @Override
    public SuccessResponse createPosBooking(CreateResortBookingRequest request,
                                            ResortEntity resortEntity,
                                            UserEntity customerEntity,
                                            BookingSourceEntity bookingSourceEntity,
                                            List<CreateResortRoomReservationRequest> roomRequests,
                                            Map<Long, ResortRoomEntity> resortRoomEntityMap,
                                            Map<Long, ReservationStatusEntity> reservationStatusEntityMap,
                                            CurrencyEntity currencyEntity) {
        ResortBookingEntity entity = ResortBookingMapper.create(request, resortEntity, customerEntity, bookingSourceEntity, generateReferenceCode());
        resortRoomReservationService.initialize(roomRequests, entity, resortRoomEntityMap, reservationStatusEntityMap, currencyEntity);
        resortBookingRepository.save(entity);
        log.info("Booking created with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    private String generateReferenceCode() {
        return "BK" + String.format("%08d", resortBookingRepository.nextReferenceCodeSequenceValue());
    }
}
