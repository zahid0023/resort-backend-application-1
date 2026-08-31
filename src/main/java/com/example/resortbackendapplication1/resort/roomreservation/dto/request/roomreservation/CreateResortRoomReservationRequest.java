package com.example.resortbackendapplication1.resort.roomreservation.dto.request.roomreservation;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.time.LocalDate;
import java.util.List;

/**
 * One room within a multi-room CreateResortBookingRequest — source/customer/currency are shared across every
 * room (booking-level, see CreateResortBookingRequest), but resort_room_category_id/resort_room_id/check_in/
 * check_out/guests/occupancy/reservation_status_id/notes are each room's own, so a single booking can hold
 * rooms in different categories with different stay windows and even different reservation statuses (e.g. one
 * room for 2 nights, another for 3, in the same request — nothing requires every room in a batch to share the
 * same status). There is no source/channel field at all — that's owned exclusively by the booking (see
 * ResortBookingEntity#getBookingSourceEntity), never duplicated per room. price_unit_id/total_price/nights are
 * also absent — ResortRoomReservationServiceImpl resolves the room's (or its category's) effective price via
 * RoomPricingResolver; a booker never types a price in directly.
 */
@Data
@EqualsAndHashCode(callSuper = false)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CreateResortRoomReservationRequest extends ResortRoomReservationRequest {

    @NotNull
    private Long resortRoomId;

    @NotNull
    private LocalDate checkIn;

    @NotNull
    private LocalDate checkOut;

    /**
     * Every occupant of this room — may differ from the booking's own customer (e.g. booking for friends).
     */
    @NotEmpty
    @Valid
    private List<CreateResortRoomReservationGuestRequest> guests;

    @NotNull
    @Min(1)
    private Integer adultCount;

    @NotNull
    @Min(0)
    private Integer childCount;

    private String notes;
}
