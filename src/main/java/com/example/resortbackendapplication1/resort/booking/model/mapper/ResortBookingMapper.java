package com.example.resortbackendapplication1.resort.booking.model.mapper;

import com.example.resortbackendapplication1.resort.booking.dto.request.booking.CreateResortBookingRequest;
import com.example.resortbackendapplication1.resort.booking.model.dto.BookingCustomerDto;
import com.example.resortbackendapplication1.resort.booking.model.dto.ResortBookingDto;
import com.example.resortbackendapplication1.resort.booking.model.entity.ResortBookingEntity;
import lombok.experimental.UtilityClass;

import java.util.UUID;

/** Booking is created only via ResortBookingService#createPosBooking (or a future channel-specific method) — there is no update() here. */
@UtilityClass
public class ResortBookingMapper {

    public ResortBookingEntity create(CreateResortBookingRequest request) {
        ResortBookingEntity entity = new ResortBookingEntity();
        entity.setNotes(request.getNotes() != null ? request.getNotes() : "");
        entity.setReferenceCode("BK" + UUID.randomUUID().toString().replace("-", "").substring(0, 10).toUpperCase());
        return entity;
    }

    /**
     * Own scalars + the customer's minimal identity only — {@code bookingSource}, {@code reservations},
     * {@code totalPrice}, and {@code currency} are chained in by the caller (see
     * {@code ResortBookingServiceImpl#toFullDto}), since they either cross into another module's mapper or are
     * derived from the booking's reservations rather than read directly off this entity.
     */
    public ResortBookingDto.ResortBookingDtoBuilder toDto(ResortBookingEntity entity) {
        return ResortBookingDto.builder()
                .id(entity.getId())
                .referenceCode(entity.getReferenceCode())
                .customer(BookingCustomerDto.builder()
                        .id(entity.getUserEntity().getId())
                        .username(entity.getUserEntity().getUsername())
                        .build())
                .notes(entity.getNotes());
    }
}
