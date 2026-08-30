package com.example.resortbackendapplication1.resort.reservation.dto.request.reservation;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.time.LocalDate;

/**
 * resort_room_id is deliberately absent — the room is resolved from the {room-id} path segment (see
 * BookingController), never supplied in the body, mirroring CreateResortRoomRequest's omission of
 * resortRoomCategoryId. price_unit_id/total_price are likewise absent — BookingController resolves the
 * room's (or its category's) effective price for currency_id and computes total_price itself via
 * PricingCalculator; a booker never types a price in directly.
 */
@Data
@EqualsAndHashCode(callSuper = false)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CreateReservationRequest extends ReservationRequest {

    /** Resolved via UserService.getUserByUsername — the customer must already be a registered platform user. */
    @NotBlank
    private String customerUsername;

    @NotNull
    private Long reservationSourceId;

    @NotNull
    private LocalDate checkIn;

    @NotNull
    private LocalDate checkOut;

    @NotNull
    @Min(1)
    private Integer adultCount;

    @NotNull
    @Min(0)
    private Integer childCount;

    @NotNull
    private Long currencyId;

    private String notes;
}
