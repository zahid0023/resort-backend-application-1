package com.example.resortbackendapplication1.resort.reservation.service;

import com.example.resortbackendapplication1.auth.model.entity.UserEntity;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.currency.model.entity.CurrencyEntity;
import com.example.resortbackendapplication1.price.model.entity.PriceUnitEntity;
import com.example.resortbackendapplication1.reservation.model.entity.ReservationSourceEntity;
import com.example.resortbackendapplication1.reservation.model.entity.ReservationStatusEntity;
import com.example.resortbackendapplication1.resort.reservation.dto.request.reservation.CreateReservationRequest;
import com.example.resortbackendapplication1.resort.reservation.dto.request.reservation.ReservationFilterRequest;
import com.example.resortbackendapplication1.resort.reservation.dto.response.reservations.ReservationResponse;
import com.example.resortbackendapplication1.resort.reservation.model.dto.ReservationDto;
import com.example.resortbackendapplication1.resort.booking.model.entity.BookingGroupEntity;
import com.example.resortbackendapplication1.resort.reservation.model.entity.ReservationEntity;
import com.example.resortbackendapplication1.resort.room.model.entity.ResortRoomEntity;

import java.math.BigDecimal;

public interface ReservationService {

    /**
     * Called by BookingController/BookingGroupController — not by ReservationController, which exposes no
     * POST endpoint (Reservation status changes are append-only; see transitionStatus below). {@code
     * totalPrice} is computed by the caller via PricingCalculator, not read off the request.
     * {@code bookingGroupEntity} is required, never null — every reservation belongs to a booking group, even
     * a lone single-room booking (a "group of one" created by BookingController); a multi-room booking (via
     * BookingGroupController) passes the same shared group for every room in the batch.
     */
    SuccessResponse create(CreateReservationRequest request,
                           UserEntity userEntity,
                           ResortRoomEntity resortRoomEntity,
                           ReservationStatusEntity reservationStatusEntity,
                           ReservationSourceEntity reservationSourceEntity,
                           CurrencyEntity currencyEntity,
                           PriceUnitEntity priceUnitEntity,
                           BigDecimal totalPrice,
                           BookingGroupEntity bookingGroupEntity);

    ReservationEntity getEntityById(Long resortRoomId, Long id);

    ReservationResponse getById(Long resortRoomId, Long id);

    PaginatedResponse<ReservationDto> getAll(Long resortRoomId, ReservationFilterRequest request);

    PaginatedResponse<ReservationDto> getAllForResort(Long resortId, ReservationFilterRequest request);

    /**
     * Append-only status change: never mutates {@code entity} in place. Soft-deletes it and inserts a new row
     * (see ReservationMapper#transition) carrying {@code newReservationStatusEntity}, linked back via
     * previous_reservation_id. Returns the NEW row's id, not {@code entity}'s.
     */
    SuccessResponse transitionStatus(ReservationEntity entity, ReservationStatusEntity newReservationStatusEntity);

    SuccessResponse delete(ReservationEntity entity);
}
