package com.example.resortbackendapplication1.resort.booking.controller;

import com.example.resortbackendapplication1.auth.model.entity.UserEntity;
import com.example.resortbackendapplication1.auth.service.UserService;
import com.example.resortbackendapplication1.currency.model.entity.CurrencyEntity;
import com.example.resortbackendapplication1.currency.service.CurrencyService;
import com.example.resortbackendapplication1.reservation.model.entity.ReservationSourceEntity;
import com.example.resortbackendapplication1.reservation.model.entity.ReservationStatusEntity;
import com.example.resortbackendapplication1.reservation.service.ReservationSourceService;
import com.example.resortbackendapplication1.reservation.service.ReservationStatusService;
import com.example.resortbackendapplication1.resort.booking.dto.request.booking.CreateBookingRequest;
import com.example.resortbackendapplication1.resort.booking.dto.request.booking.CreateBookingRoomEntryRequest;
import com.example.resortbackendapplication1.resort.booking.model.entity.BookingGroupEntity;
import com.example.resortbackendapplication1.resort.pricing.RoomPricingResolver;
import com.example.resortbackendapplication1.resort.booking.service.BookingGroupService;
import com.example.resortbackendapplication1.resort.core.model.entity.ResortEntity;
import com.example.resortbackendapplication1.resort.core.service.ResortService;
import com.example.resortbackendapplication1.resort.reservation.dto.request.reservation.CreateReservationRequest;
import com.example.resortbackendapplication1.resort.reservation.service.ReservationService;
import com.example.resortbackendapplication1.resort.room.model.entity.ResortRoomEntity;
import com.example.resortbackendapplication1.resort.room.service.ResortRoomService;
import com.example.resortbackendapplication1.resort.roomcategory.model.entity.ResortRoomCategoryEntity;
import com.example.resortbackendapplication1.resort.roomcategory.service.ResortRoomCategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Books 2+ rooms — possibly across different resort room categories, since a booking can't stay scoped to one
 * room-category's URL if it spans categories — in one transaction, tagged with one new BookingGroupEntity so
 * they can be shown together afterward. See BookingController for the single-room equivalent (which now also
 * creates a "group of one", for consistency — every reservation belongs to a booking group).
 *
 * <p>check_in/check_out/reservation_status_id/reservation_source_id/customer_username/notes are shared across
 * every room in the request; each room entry resolves its own effective price (room override, else category's)
 * via the same RoomPricingResolver BookingController uses, so pricing can never drift between the two
 * entry points.
 */
@RestController
@RequestMapping("/api/v1/resorts/{resort-id}/bookings")
public class BookingGroupController {

    private final BookingGroupService bookingGroupService;
    private final ReservationService reservationService;
    private final ResortService resortService;
    private final ResortRoomCategoryService resortRoomCategoryService;
    private final ResortRoomService resortRoomService;
    private final RoomPricingResolver roomPricingResolver;
    private final UserService userService;
    private final ReservationStatusService reservationStatusService;
    private final ReservationSourceService reservationSourceService;
    private final CurrencyService currencyService;

    public BookingGroupController(BookingGroupService bookingGroupService,
                                  ReservationService reservationService,
                                  ResortService resortService,
                                  ResortRoomCategoryService resortRoomCategoryService,
                                  ResortRoomService resortRoomService,
                                  RoomPricingResolver roomPricingResolver,
                                  UserService userService,
                                  ReservationStatusService reservationStatusService,
                                  ReservationSourceService reservationSourceService,
                                  CurrencyService currencyService) {
        this.bookingGroupService = bookingGroupService;
        this.reservationService = reservationService;
        this.resortService = resortService;
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
            @Valid @RequestBody CreateBookingRequest request) {
        ResortEntity resortEntity = resortService.getEntityById(resortId);

        UserEntity customerEntity = userService.getUserByUsername(request.getCustomerUsername());
        ReservationStatusEntity reservationStatusEntity = reservationStatusService.getEntityById(request.getReservationStatusId());
        ReservationSourceEntity reservationSourceEntity = reservationSourceService.getEntityById(request.getReservationSourceId());

        Long bookingGroupId = bookingGroupService.create(resortEntity, customerEntity).getId();
        BookingGroupEntity bookingGroupEntity = bookingGroupService.getEntityById(resortId, bookingGroupId);

        for (CreateBookingRoomEntryRequest roomEntry : request.getRooms()) {
            createRoomReservation(resortId, resortEntity, request, roomEntry, customerEntity,
                    reservationStatusEntity, reservationSourceEntity, bookingGroupEntity);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(bookingGroupService.getById(resortId, bookingGroupId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(
            @PathVariable("resort-id") Long resortId,
            @PathVariable Long id) {
        resortService.getEntityById(resortId);
        return ResponseEntity.ok(bookingGroupService.getById(resortId, id));
    }

    private void createRoomReservation(Long resortId, ResortEntity resortEntity, CreateBookingRequest request,
                                       CreateBookingRoomEntryRequest roomEntry, UserEntity customerEntity,
                                       ReservationStatusEntity reservationStatusEntity,
                                       ReservationSourceEntity reservationSourceEntity,
                                       BookingGroupEntity bookingGroupEntity) {
        ResortRoomCategoryEntity resortRoomCategoryEntity =
                resortRoomCategoryService.getEntityById(resortId, roomEntry.getResortRoomCategoryId());
        ResortRoomEntity resortRoomEntity =
                resortRoomService.getEntityById(roomEntry.getResortRoomCategoryId(), roomEntry.getRoomId());
        CurrencyEntity currencyEntity = currencyService.getEntityById(roomEntry.getCurrencyId());

        RoomPricingResolver.Result pricing = roomPricingResolver.resolve(
                resortEntity, resortRoomEntity, roomEntry.getResortRoomCategoryId(), currencyEntity.getId(),
                request.getCheckIn(), request.getCheckOut());

        CreateReservationRequest reservationRequest = new CreateReservationRequest();
        reservationRequest.setReservationStatusId(request.getReservationStatusId());
        reservationRequest.setCustomerUsername(request.getCustomerUsername());
        reservationRequest.setReservationSourceId(request.getReservationSourceId());
        reservationRequest.setCheckIn(request.getCheckIn());
        reservationRequest.setCheckOut(request.getCheckOut());
        reservationRequest.setAdultCount(roomEntry.getAdultCount());
        reservationRequest.setChildCount(roomEntry.getChildCount());
        reservationRequest.setCurrencyId(roomEntry.getCurrencyId());
        reservationRequest.setNotes(request.getNotes());

        reservationService.create(reservationRequest, customerEntity, resortRoomEntity, reservationStatusEntity,
                reservationSourceEntity, currencyEntity, pricing.priceUnitEntity(), pricing.totalPrice(), bookingGroupEntity);
    }
}
