package com.example.resortbackendapplication1.resort.booking.model.mapper;

import com.example.resortbackendapplication1.auth.model.entity.UserEntity;
import com.example.resortbackendapplication1.bookingsource.model.entity.BookingSourceEntity;
import com.example.resortbackendapplication1.resort.booking.dto.request.booking.CreateResortBookingRequest;
import com.example.resortbackendapplication1.resort.booking.model.entity.ResortBookingEntity;
import com.example.resortbackendapplication1.resort.core.model.entity.ResortEntity;
import lombok.experimental.UtilityClass;

/** Booking is created only via ResortBookingService#createPosBooking (or a future channel-specific method) — there is no update() here. */
@UtilityClass
public class ResortBookingMapper {

    public ResortBookingEntity create(CreateResortBookingRequest request, ResortEntity resortEntity, UserEntity customerEntity,
                                      BookingSourceEntity bookingSourceEntity, String referenceCode) {
        ResortBookingEntity entity = new ResortBookingEntity();
        entity.setResortEntity(resortEntity);
        entity.setUserEntity(customerEntity);
        entity.setBookingSourceEntity(bookingSourceEntity);
        entity.setNotes(request.getNotes() != null ? request.getNotes() : "");
        entity.setReferenceCode(referenceCode);
        return entity;
    }
}
