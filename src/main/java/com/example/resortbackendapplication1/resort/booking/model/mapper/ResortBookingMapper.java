package com.example.resortbackendapplication1.resort.booking.model.mapper;

import com.example.resortbackendapplication1.resort.booking.dto.request.booking.CreateResortBookingRequest;
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
}
