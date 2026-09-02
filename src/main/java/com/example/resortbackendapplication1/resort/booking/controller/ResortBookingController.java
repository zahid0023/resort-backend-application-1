package com.example.resortbackendapplication1.resort.booking.controller;

import com.example.resortbackendapplication1.auth.dto.request.RegistrationRequest;
import com.example.resortbackendapplication1.auth.model.entity.UserEntity;
import com.example.resortbackendapplication1.auth.service.UserService;
import com.example.resortbackendapplication1.bookingsource.model.entity.BookingSourceEntity;
import com.example.resortbackendapplication1.bookingsource.service.BookingSourceService;
import com.example.resortbackendapplication1.currency.model.entity.CurrencyEntity;
import com.example.resortbackendapplication1.currency.service.CurrencyService;
import com.example.resortbackendapplication1.reservation.model.entity.ReservationStatusEntity;
import com.example.resortbackendapplication1.reservation.service.ReservationStatusService;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.resort.booking.dto.request.booking.CreateResortBookingRequest;
import com.example.resortbackendapplication1.resort.booking.service.ResortBookingService;
import com.example.resortbackendapplication1.resort.core.model.entity.ResortEntity;
import com.example.resortbackendapplication1.resort.core.service.ResortService;
import com.example.resortbackendapplication1.resort.roomreservation.dto.request.roomreservation.CreateResortRoomReservationRequest;
import com.example.resortbackendapplication1.resort.room.model.entity.ResortRoomEntity;
import com.example.resortbackendapplication1.resort.room.service.ResortRoomService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.SecureRandom;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * The single booking entry point, for 1 or more rooms — possibly across different resort room categories, since
 * a booking can't stay scoped to one room-category's URL if it spans categories. A single-room booking is just
 * a {@code rooms} list of size 1; every reservation, whether booked alone or alongside others, is tagged with
 * one new ResortBookingEntity so they can be shown together afterward (a "group of one" for a lone room, for
 * consistency — every reservation belongs to a booking).
 *
 * <p>booking_source_id/email/phone_number/notes/currency_id are booking-level — booking_source_id in particular
 * is owned exclusively by the booking, never duplicated per room (a room resolves its channel by reaching
 * through its booking); every room in a booking shares one currency. check_in/check_out/reservation_status_id/
 * notes on each room entry are that room's own, so rooms in the same booking need not share a stay window or
 * even a reservation status; each room's effective price (room override, else category's) is resolved inside
 * {@code ResortRoomReservationServiceImpl}, via {@code RoomPricingResolver}, not here.
 *
 * <p>The customer is resolved by a find-or-create on username: if {@code email} is present, it (not
 * {@code phone_number}) is used as the username to look up/create the customer; otherwise {@code phone_number}
 * is used. If no user exists yet for that username, one is registered on the fly with a random password (see
 * {@link #registerCustomer}) rather than requiring the booker to register the customer separately first.
 */
@RestController
@RequestMapping("/api/v1/resorts/{resort-id}/bookings")
public class ResortBookingController {

    private final ResortBookingService resortBookingService;
    private final ResortService resortService;
    private final ResortRoomService resortRoomService;
    private final UserService userService;
    private final ReservationStatusService reservationStatusService;
    private final BookingSourceService bookingSourceService;
    private final CurrencyService currencyService;

    public ResortBookingController(ResortBookingService resortBookingService,
                                   ResortService resortService,
                                   ResortRoomService resortRoomService,
                                   UserService userService,
                                   ReservationStatusService reservationStatusService,
                                   BookingSourceService bookingSourceService,
                                   CurrencyService currencyService) {
        this.resortBookingService = resortBookingService;
        this.resortService = resortService;
        this.resortRoomService = resortRoomService;
        this.userService = userService;
        this.reservationStatusService = reservationStatusService;
        this.bookingSourceService = bookingSourceService;
        this.currencyService = currencyService;
    }

    @PostMapping("/pos")
    public ResponseEntity<?> createPosBooking(
            @PathVariable("resort-id") Long resortId,
            @Valid @RequestBody CreateResortBookingRequest request) {
        ResortEntity resortEntity = resortService.getEntityById(resortId);

        String userName = request.getEmail() != null && !request.getEmail().isEmpty()
                ? request.getEmail()
                : request.getPhoneNumber();

        UserEntity userEntity = userService.existsByUsername(userName)
                ? userService.getUserByUsername(userName)
                : registerCustomer(userName);

        BookingSourceEntity bookingSourceEntity = bookingSourceService.getEntityById(request.getBookingSourceId());
        CurrencyEntity currencyEntity = currencyService.getEntityById(request.getCurrencyId());

        Map<Long, ResortRoomEntity> resortRoomEntityMap = request.getRooms().stream()
                .map(CreateResortRoomReservationRequest::getResortRoomId)
                .distinct()
                .collect(Collectors.toMap(Function.identity(), roomId -> resortRoomService.getEntityByResortId(resortId, roomId)));

        Map<Long, ReservationStatusEntity> reservationStatusEntityMap = request.getRooms().stream()
                .map(CreateResortRoomReservationRequest::getReservationStatusId)
                .distinct()
                .collect(Collectors.toMap(Function.identity(), reservationStatusService::getEntityById));

        SuccessResponse response = resortBookingService.createPosBooking(
                request, resortEntity, userEntity, bookingSourceEntity, request.getRooms(),
                resortRoomEntityMap, reservationStatusEntityMap, currencyEntity);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    private static final String PASSWORD_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int PASSWORD_LENGTH = 7;
    private static final SecureRandom PASSWORD_RANDOM = new SecureRandom();

    /**
     * Registers a new customer on the fly with {@code userName} (the email or phone number the booker supplied)
     * as the username and a random password — the manual reservation flow's find-or-create: if no user exists
     * yet for that email/phone, one is created here rather than requiring the booker to register the customer
     * separately first.
     */
    private UserEntity registerCustomer(String userName) {
        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setUserName(userName);
        String password = generateRandomPassword();
        registrationRequest.setPassword(password);
        registrationRequest.setConfirmPassword(password);
        SuccessResponse response = userService.registerUser(registrationRequest);
        return userService.getUserById(response.getId());
    }

    private String generateRandomPassword() {
        return PASSWORD_RANDOM.ints(PASSWORD_LENGTH, 0, PASSWORD_CHARACTERS.length())
                .mapToObj(PASSWORD_CHARACTERS::charAt)
                .map(String::valueOf)
                .collect(Collectors.joining());
    }
}
