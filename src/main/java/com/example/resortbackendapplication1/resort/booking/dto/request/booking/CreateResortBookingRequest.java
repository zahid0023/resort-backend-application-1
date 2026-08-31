package com.example.resortbackendapplication1.resort.booking.dto.request.booking;

import com.example.resortbackendapplication1.resort.roomreservation.dto.request.roomreservation.CreateResortRoomReservationRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.util.List;

/**
 * Books 2+ rooms — possibly across different resort room categories — in one transaction, tagged with one new
 * ResortBookingEntity. booking_source_id/customer_username/notes/currency_id are booking-level, owned
 * exclusively by the booking (never duplicated per room — see ResortBookingEntity#getBookingSourceEntity; every
 * room in a booking is charged in the same currency). Each entry in {@code rooms} picks its own room+dates+
 * occupancy+reservation_status_id+notes (see CreateResortRoomReservationRequest) — rooms in the same booking
 * are not required to share a stay window or even a reservation status.
 */
@Data
@EqualsAndHashCode(callSuper = false)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CreateResortBookingRequest extends ResortBookingRequest {

    @NotBlank
    private String customerUsername;

    @NotNull
    private Long bookingSourceId;

    @NotNull
    private Long currencyId;

    private String notes;

    @NotEmpty
    @Valid
    private List<CreateResortRoomReservationRequest> rooms;
}
