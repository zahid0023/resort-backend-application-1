package com.example.resortbackendapplication1.resort.booking.dto.request.booking;

import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

/**
 * Base of the {@code Create}/(future {@code Update}) request pair, per this codebase's per-entity request
 * convention — currently empty since every booking-level field so far (booking_source_id/customer_username/
 * notes/currency_id) is create-only/immutable; reservation_status_id lives per-room instead (see
 * CreateResortRoomReservationRequest), not here, since rooms in the same booking aren't required to share one.
 */
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResortBookingRequest {
}
