package com.example.resortbackendapplication1.resort.booking.service;

import com.example.resortbackendapplication1.auth.model.entity.UserEntity;
import com.example.resortbackendapplication1.bookingsource.model.entity.BookingSourceEntity;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.currency.model.entity.CurrencyEntity;
import com.example.resortbackendapplication1.reservation.model.entity.ReservationStatusEntity;
import com.example.resortbackendapplication1.resort.booking.dto.request.booking.CreateResortBookingRequest;
import com.example.resortbackendapplication1.resort.core.model.entity.ResortEntity;
import com.example.resortbackendapplication1.resort.room.model.entity.ResortRoomEntity;
import com.example.resortbackendapplication1.resort.roomreservation.dto.request.roomreservation.CreateResortRoomReservationRequest;

import java.util.List;
import java.util.Map;

public interface ResortBookingService {

    /**
     * Entry point for the POS (booker) channel specifically — a staff member creating a booking on a
     * customer's behalf over WhatsApp/phone/walk-in/etc. (see {@code ResortBookingController}). Deliberately
     * not a single generic {@code create}: a future channel (website self-service, an OTA integration) is
     * expected to have its own create method here instead of overloading this one, since each channel can
     * legitimately need different inputs/validation (e.g. a website booking authenticates its own customer
     * rather than a booker looking one up by username). Every booking — even a single-room one, a "group of
     * one" — starts from exactly one of these channel-specific methods.
     *
     * <p>Unlike every other ServiceImpl in this codebase, {@code ResortBookingServiceImpl} is allowed to inject
     * {@code ResortRoomReservationService} directly (a deliberate, named exception to the usual
     * controller-orchestrates-cross-domain rule) — a reservation's whole purpose is to serve availability and
     * booking, so booking is the one caller allowed to reach it directly rather than going through the
     * controller. {@code request}/{@code resortEntity}/{@code userEntity}/{@code bookingSourceEntity} are
     * booking-level; {@code roomRequests} plus the two lookup maps are handed straight through to
     * {@code ResortRoomReservationService#attachReservationEntities} so this method can attach the reservations to the new
     * booking and let the save cascade insert everything in one transaction. {@code currencyEntity} is shared
     * by every room, since a booking is charged in one currency.
     *
     * @param userEntity the customer the booking is for, looked up by username
     * @return a SuccessResponse with {@code id} set to the new booking's id — there is no GET /{id} endpoint to
     * follow up with (removed deliberately, see ResortBookingController), so the response body doesn't need to
     * carry the full nested booking either
     */
    SuccessResponse createPosBooking(CreateResortBookingRequest request,
                                     ResortEntity resortEntity,
                                     UserEntity userEntity,
                                     BookingSourceEntity bookingSourceEntity,
                                     List<CreateResortRoomReservationRequest> roomRequests,
                                     Map<Long, ResortRoomEntity> resortRoomEntityMap,
                                     Map<Long, ReservationStatusEntity> reservationStatusEntityMap,
                                     CurrencyEntity currencyEntity);
}
