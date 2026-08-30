package com.example.resortbackendapplication1.resort.booking.dto.request.booking;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.time.LocalDate;
import java.util.List;

/**
 * Books 2+ rooms — possibly across different resort room categories — in one transaction, tagged with one new
 * BookingGroupEntity. check_in/check_out/reservation_status_id/reservation_source_id/customer_username/notes
 * are shared by every room; each entry in {@code rooms} picks its own category+room+occupancy+currency (see
 * CreateBookingRoomEntryRequest).
 */
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CreateBookingRequest {

    @NotBlank
    private String customerUsername;

    @NotNull
    private Long reservationStatusId;

    @NotNull
    private Long reservationSourceId;

    @NotNull
    private LocalDate checkIn;

    @NotNull
    private LocalDate checkOut;

    private String notes;

    @NotEmpty
    @Valid
    private List<CreateBookingRoomEntryRequest> rooms;
}
