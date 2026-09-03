package com.example.resortbackendapplication1.resort.booking.dto.request.booking;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * No field was classified as Filterable — bookings are reached via the resort-scoped paginated finder on
 * ResortBookingRepository, not the generic Specification framework — so this only carries pagination/sort, no
 * toPredicates/Filterable implementation, no ResortBookingSpecification.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ResortBookingFilterRequest extends PaginatedRequest {
}
