package com.example.resortbackendapplication1.resort.roomreservation.dto.request.roomreservation;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * No field was classified as Filterable during scoping (a room reservation is reached via the room-scoped /
 * resort-scoped paginated finders on ResortRoomReservationRepository, not the generic Specification framework), so
 * this only carries pagination/sort — no toPredicates/Filterable implementation, no RoomReservationSpecification.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ResortRoomReservationFilterRequest extends PaginatedRequest {
}
