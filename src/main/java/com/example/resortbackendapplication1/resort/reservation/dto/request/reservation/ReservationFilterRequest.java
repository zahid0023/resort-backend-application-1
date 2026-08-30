package com.example.resortbackendapplication1.resort.reservation.dto.request.reservation;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * No field was classified as Filterable during scoping (Reservation is reached via the room-scoped /
 * resort-scoped paginated finders on ReservationRepository, not the generic Specification framework), so this
 * only carries pagination/sort — no toPredicates/Filterable implementation, no ReservationSpecification.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ReservationFilterRequest extends PaginatedRequest {
}
