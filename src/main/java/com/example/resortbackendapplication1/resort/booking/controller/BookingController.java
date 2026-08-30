package com.example.resortbackendapplication1.resort.booking.controller;

import com.example.resortbackendapplication1.auth.model.entity.UserEntity;
import com.example.resortbackendapplication1.auth.service.UserService;
import com.example.resortbackendapplication1.currency.model.entity.CurrencyEntity;
import com.example.resortbackendapplication1.currency.service.CurrencyService;
import com.example.resortbackendapplication1.reservation.model.entity.ReservationSourceEntity;
import com.example.resortbackendapplication1.reservation.model.entity.ReservationStatusEntity;
import com.example.resortbackendapplication1.reservation.service.ReservationSourceService;
import com.example.resortbackendapplication1.reservation.service.ReservationStatusService;
import com.example.resortbackendapplication1.resort.booking.model.entity.BookingGroupEntity;
import com.example.resortbackendapplication1.resort.pricing.RoomPricingResolver;
import com.example.resortbackendapplication1.resort.booking.service.BookingGroupService;
import com.example.resortbackendapplication1.resort.reservation.dto.request.reservation.CreateReservationRequest;
import com.example.resortbackendapplication1.resort.reservation.service.ReservationService;
import com.example.resortbackendapplication1.resort.room.model.entity.ResortRoomEntity;
import com.example.resortbackendapplication1.resort.room.service.ResortRoomService;
import com.example.resortbackendapplication1.resort.roomcategory.model.entity.ResortRoomCategoryEntity;
import com.example.resortbackendapplication1.resort.roomcategory.service.ResortRoomCategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The POS/booker entry point onto the single, channel-independent Reservation domain (see docs/availability-api.md's
 * Scenario section) — a resort booker, already having checked availability via AvailabilityController, books a
 * specific room for a customer on behalf of a WhatsApp/phone/walk-in/etc. conversation. This is deliberately not
 * on ReservationController, which exposes no POST (Reservation itself is append-only/channel-agnostic — see
 * ReservationController's own javadoc). {@code total_price} is never taken from the request — it's computed by
 * RoomPricingResolver from the room's (or its category's) currently active price rows.
 *
 * <p>Every reservation belongs to a booking group, even one booked here alone — this endpoint creates a
 * "group of one" via BookingGroupService before creating the reservation, for consistency with
 * BookingGroupController's multi-room flow (see that controller for booking 2+ rooms, possibly across
 * different categories, in one call).
 */
@RestController
@RequestMapping("/api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/rooms/{room-id}/reservations")
public class BookingController {

    private final ReservationService reservationService;
    private final BookingGroupService bookingGroupService;
    private final ResortRoomCategoryService resortRoomCategoryService;
    private final ResortRoomService resortRoomService;
    private final RoomPricingResolver roomPricingResolver;
    private final UserService userService;
    private final ReservationStatusService reservationStatusService;
    private final ReservationSourceService reservationSourceService;
    private final CurrencyService currencyService;

    public BookingController(ReservationService reservationService,
                             BookingGroupService bookingGroupService,
                             ResortRoomCategoryService resortRoomCategoryService,
                             ResortRoomService resortRoomService,
                             RoomPricingResolver roomPricingResolver,
                             UserService userService,
                             ReservationStatusService reservationStatusService,
                             ReservationSourceService reservationSourceService,
                             CurrencyService currencyService) {
        this.reservationService = reservationService;
        this.bookingGroupService = bookingGroupService;
        this.resortRoomCategoryService = resortRoomCategoryService;
        this.resortRoomService = resortRoomService;
        this.roomPricingResolver = roomPricingResolver;
        this.userService = userService;
        this.reservationStatusService = reservationStatusService;
        this.reservationSourceService = reservationSourceService;
        this.currencyService = currencyService;
    }

    @PostMapping
    public ResponseEntity<?> create(
            @PathVariable("resort-id") Long resortId,
            @PathVariable("resort-room-category-id") Long resortRoomCategoryId,
            @PathVariable("room-id") Long roomId,
            @Valid @RequestBody CreateReservationRequest request) {
        ResortRoomCategoryEntity resortRoomCategoryEntity = resortRoomCategoryService.getEntityById(resortId, resortRoomCategoryId);
        ResortRoomEntity resortRoomEntity = resortRoomService.getEntityById(resortRoomCategoryId, roomId);

        UserEntity customerEntity = userService.getUserByUsername(request.getCustomerUsername());
        ReservationStatusEntity reservationStatusEntity = reservationStatusService.getEntityById(request.getReservationStatusId());
        ReservationSourceEntity reservationSourceEntity = reservationSourceService.getEntityById(request.getReservationSourceId());
        CurrencyEntity currencyEntity = currencyService.getEntityById(request.getCurrencyId());

        Long bookingGroupId = bookingGroupService.create(resortRoomCategoryEntity.getResortEntity(), customerEntity).getId();
        BookingGroupEntity bookingGroupEntity = bookingGroupService.getEntityById(resortId, bookingGroupId);

        RoomPricingResolver.Result pricing = roomPricingResolver.resolve(
                resortRoomCategoryEntity.getResortEntity(), resortRoomEntity, resortRoomCategoryId,
                currencyEntity.getId(), request.getCheckIn(), request.getCheckOut());

        return ResponseEntity.status(HttpStatus.CREATED).body(
                reservationService.create(request, customerEntity, resortRoomEntity, reservationStatusEntity,
                        reservationSourceEntity, currencyEntity, pricing.priceUnitEntity(), pricing.totalPrice(),
                        bookingGroupEntity));
    }
}
