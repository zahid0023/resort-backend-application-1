package com.example.resortbackendapplication1.resort.booking.controller;

import com.example.resortbackendapplication1.auth.dto.request.RegistrationRequest;
import com.example.resortbackendapplication1.auth.model.entity.UserEntity;
import com.example.resortbackendapplication1.auth.service.UserService;
import com.example.resortbackendapplication1.bookingsource.model.entity.BookingSourceEntity;
import com.example.resortbackendapplication1.bookingsource.service.BookingSourceService;
import com.example.resortbackendapplication1.commons.mail.MailProviderConfigCode;
import com.example.resortbackendapplication1.commons.utils.PasswordUtils;
import com.example.resortbackendapplication1.currency.model.entity.CurrencyEntity;
import com.example.resortbackendapplication1.currency.service.CurrencyService;
import com.example.resortbackendapplication1.mail.provider.model.entity.MailProviderConfigEntity;
import com.example.resortbackendapplication1.mail.provider.service.MailProviderConfigService;
import com.example.resortbackendapplication1.mail.send.service.MailSendService;
import com.example.resortbackendapplication1.reservation.model.entity.ReservationStatusEntity;
import com.example.resortbackendapplication1.reservation.service.ReservationStatusService;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.resort.booking.dto.request.booking.CreateResortBookingRequest;
import com.example.resortbackendapplication1.resort.booking.dto.request.booking.ResortBookingFilterRequest;
import com.example.resortbackendapplication1.resort.booking.service.ResortBookingService;
import com.example.resortbackendapplication1.resort.core.model.entity.ResortEntity;
import com.example.resortbackendapplication1.resort.core.service.ResortService;
import com.example.resortbackendapplication1.resort.roomreservation.dto.request.roomreservation.CreateResortRoomReservationRequest;
import com.example.resortbackendapplication1.resort.room.model.entity.ResortRoomEntity;
import com.example.resortbackendapplication1.resort.room.service.ResortRoomService;
import com.example.resortbackendapplication1.whatsapp.send.service.WhatsAppSendService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;
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
 * {@link #registerCustomer}) rather than requiring the booker to register the customer separately first. The
 * random password itself is never sent anywhere — instead, once the booking succeeds (not at registration
 * time), the customer gets a notification pointing them at the (placeholder, env-configured) customer portal
 * and telling them to use "forgot password" to set their own — email via {@link #sendBookingNotificationEmail}
 * when registered by email, WhatsApp via {@link #sendBookingNotificationWhatsApp} when registered by phone and
 * that phone is WhatsApp-reachable. See {@code PasswordResetController} for the actual forgot-password flow.
 */
@Slf4j
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
    private final MailProviderConfigService mailProviderConfigService;
    private final MailSendService mailSendService;
    private final WhatsAppSendService whatsAppSendService;

    @Value("${app.customer-portal-base-url}")
    private String customerPortalBaseUrl;

    public ResortBookingController(ResortBookingService resortBookingService,
                                   ResortService resortService,
                                   ResortRoomService resortRoomService,
                                   UserService userService,
                                   ReservationStatusService reservationStatusService,
                                   BookingSourceService bookingSourceService,
                                   CurrencyService currencyService,
                                   MailProviderConfigService mailProviderConfigService,
                                   MailSendService mailSendService,
                                   WhatsAppSendService whatsAppSendService) {
        this.resortBookingService = resortBookingService;
        this.resortService = resortService;
        this.resortRoomService = resortRoomService;
        this.userService = userService;
        this.reservationStatusService = reservationStatusService;
        this.bookingSourceService = bookingSourceService;
        this.currencyService = currencyService;
        this.mailProviderConfigService = mailProviderConfigService;
        this.mailSendService = mailSendService;
        this.whatsAppSendService = whatsAppSendService;
    }

    @PostMapping("/pos")
    public ResponseEntity<?> createPosBooking(
            @PathVariable("resort-id") Long resortId,
            @Valid @RequestBody CreateResortBookingRequest request) {
        ResortEntity resortEntity = resortService.getEntityById(resortId);

        boolean usingEmail = request.getEmail() != null && !request.getEmail().isEmpty();
        String userName = usingEmail ? request.getEmail() : request.getPhoneNumber();

        boolean isNewCustomer = !userService.existsByUsername(userName);
        String generatedPassword = isNewCustomer ? PasswordUtils.generateRandomPassword() : null;
        UserEntity userEntity = isNewCustomer
                ? registerCustomer(userName, generatedPassword)
                : userService.getUserByUsername(userName);

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


        if (usingEmail) {
            sendBookingNotificationEmail(userName);
        } else {
            sendBookingNotificationWhatsApp(userEntity, userName);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /** The resort-wide, paginated, detailed booking view — reference code, customer, source, and every room. */
    @GetMapping
    public ResponseEntity<?> getAll(
            @PathVariable("resort-id") Long resortId,
            @Valid @ParameterObject ResortBookingFilterRequest request) {
        resortService.getEntityById(resortId);
        return ResponseEntity.ok(resortBookingService.getAll(resortId, request));
    }

    /**
     * Registers a new customer on the fly with {@code userName} (the email or phone number the booker supplied)
     * as the username and {@code password} (a random value the customer never sees — the account is only ever
     * usable once they run the forgot-password flow) — the manual reservation flow's find-or-create: if no user
     * exists yet for that email/phone, one is created here rather than requiring the booker to register the
     * customer separately first. Deliberately does <b>not</b> send the booking notification itself — the caller
     * only does so (via {@link #sendBookingNotificationEmail}/{@link #sendBookingNotificationWhatsApp}) after
     * {@code ResortBookingService#createPosBooking} has actually succeeded, so a customer who registers but
     * whose booking then fails (e.g. room no longer available) never receives a notification for a booking that
     * doesn't exist.
     */
    private UserEntity registerCustomer(String userName, String password) {
        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setUserName(userName);
        registrationRequest.setPassword(password);
        registrationRequest.setConfirmPassword(password);
        SuccessResponse response = userService.registerUser(registrationRequest);
        return userService.getUserById(response.getId());
    }

    /**
     * Sent on every booking, new customer or returning — just the username and where to track the booking.
     * Best-effort — a missing {@link MailProviderConfigCode#CREATE_USER_EMAIL_NOTIFICATIONS} config, or a
     * failure while sending through it, is logged and swallowed rather than failing the booking itself. Never
     * includes the password (generated or otherwise) — see {@code PasswordResetController} for how a customer
     * actually sets one.
     */
    private void sendBookingNotificationEmail(String email) {
        Optional<MailProviderConfigEntity> configEntity =
                mailProviderConfigService.getEntityByCode(MailProviderConfigCode.CREATE_USER_EMAIL_NOTIFICATIONS);
        if (configEntity.isEmpty()) {
            log.warn("No MailProviderConfig configured for code {}; skipping booking notification email to {}",
                    MailProviderConfigCode.CREATE_USER_EMAIL_NOTIFICATIONS, email);
            return;
        }
        try {
            mailSendService.send(configEntity.get(), email, "Track Your Booking", bookingNotificationMessage(email));
        } catch (Exception ex) {
            log.error("Failed to send booking notification email to {}: {}", email, ex.getMessage());
        }
    }

    /**
     * Best-effort, mirroring {@link #sendBookingNotificationEmail} — skipped (not failed) when the registered
     * phone isn't WhatsApp-reachable, since there is no SMS fallback.
     */
    private void sendBookingNotificationWhatsApp(UserEntity userEntity, String phoneNumber) {
        boolean isWhatsappReachable = userEntity.getUserPhoneEntities().stream()
                .anyMatch(phone -> phone.getPhone().equals(phoneNumber) && Boolean.TRUE.equals(phone.getIsWhatsapp()));
        if (!isWhatsappReachable) {
            log.info("Phone {} is not WhatsApp-reachable; skipping booking notification WhatsApp message", phoneNumber);
            return;
        }
        try {
            whatsAppSendService.send(phoneNumber, bookingNotificationMessage(phoneNumber));
        } catch (Exception ex) {
            log.error("Failed to send booking notification WhatsApp message to {}: {}", phoneNumber, ex.getMessage());
        }
    }

    private String bookingNotificationMessage(String username) {
        return "Your booking username is " + username + ". Track your booking at " + customerPortalBaseUrl + ".";
    }
}
