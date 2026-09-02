package com.example.resortbackendapplication1.resort.roomreservation.service;

import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.currency.model.entity.CurrencyEntity;
import com.example.resortbackendapplication1.reservation.model.entity.ReservationStatusEntity;
import com.example.resortbackendapplication1.resort.room.model.entity.ResortRoomEntity;
import com.example.resortbackendapplication1.resort.roomreservation.dto.request.roomreservation.CreateResortRoomReservationRequest;
import com.example.resortbackendapplication1.resort.roomreservation.dto.request.roomreservation.ResortRoomReservationFilterRequest;
import com.example.resortbackendapplication1.resort.roomreservation.model.dto.ResortRoomReservationDto;
import com.example.resortbackendapplication1.resort.booking.model.entity.ResortBookingEntity;

import java.util.List;
import java.util.Map;

public interface ResortRoomReservationService {

    /**
     * Builds one {@code ResortRoomReservationEntity} per entry in {@code request} and attaches each to both
     * {@code resortBookingEntity} and its own {@code resortRoomEntity} (looked up from {@code resortRoomEntityMap}
     * by {@code resort_room_id}) — it does not save anything itself, hence "attachReservationEntities" rather than "create".
     * {@code resortRoomReservationEntities} is owned by the booking (cascade = ALL, see {@code ResortBookingEntity}),
     * so persisting the booking (in {@code ResortBookingServiceImpl#createPosBooking}) is what actually inserts
     * these rows, transitively cascading their own owned guests/nightly prices too. There is no separate
     * single-item overload — a single-room booking calls this too, with a one-element list, so every caller
     * integrates against one entry point regardless of how many rooms are being booked.
     *
     * <p>{@code reservationStatusEntityMap} is looked up per room by that room's own
     * {@code reservation_status_id} — nothing requires every room in a batch to share the same status.
     * {@code currencyEntity} is shared by every room, since a booking is charged in one currency. Each room's
     * price unit/nightly rates are resolved internally (via {@code RoomPricingResolver}), not passed in.
     * {@code resortBookingEntity} is shared by every room in the batch. There is no customer parameter — the
     * customer is owned by the booking, reached via {@code resortBookingEntity.getUserEntity()}, not duplicated here.
     * Built entities are reachable afterward via {@code resortBookingEntity.getResortRoomReservationEntities()} —
     * still transient/unsaved until the caller persists {@code resortBookingEntity}.
     */
    void attachReservationEntities(List<CreateResortRoomReservationRequest> request,
                            ResortBookingEntity resortBookingEntity,
                            Map<Long, ResortRoomEntity> resortRoomEntityMap,
                            Map<Long, ReservationStatusEntity> reservationStatusEntityMap,
                            CurrencyEntity currencyEntity);

    PaginatedResponse<ResortRoomReservationDto> getAll(Long resortId, ResortRoomReservationFilterRequest request);
}
