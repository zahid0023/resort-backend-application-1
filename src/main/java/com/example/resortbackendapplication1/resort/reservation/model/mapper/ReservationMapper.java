package com.example.resortbackendapplication1.resort.reservation.model.mapper;

import com.example.resortbackendapplication1.currency.model.entity.CurrencyEntity;
import com.example.resortbackendapplication1.price.model.entity.PriceUnitEntity;
import com.example.resortbackendapplication1.reservation.model.entity.ReservationSourceEntity;
import com.example.resortbackendapplication1.reservation.model.entity.ReservationStatusEntity;
import com.example.resortbackendapplication1.resort.reservation.dto.request.reservation.CreateReservationRequest;
import com.example.resortbackendapplication1.resort.reservation.model.dto.ReservationDto;
import com.example.resortbackendapplication1.resort.reservation.model.entity.ReservationEntity;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;

/**
 * Reservation is append-only: there is no update() here. A status change never mutates a row — see
 * transition(), which builds the new row that supersedes an existing one (ServiceImpl soft-deletes the old
 * row and saves both).
 */
@UtilityClass
public class ReservationMapper {

    public ReservationEntity create(CreateReservationRequest request,
                                    ReservationStatusEntity reservationStatusEntity,
                                    ReservationSourceEntity reservationSourceEntity,
                                    CurrencyEntity currencyEntity,
                                    PriceUnitEntity priceUnitEntity,
                                    BigDecimal totalPrice) {
        ReservationEntity entity = new ReservationEntity();
        entity.setReservationStatusEntity(reservationStatusEntity);
        entity.setReservationSourceEntity(reservationSourceEntity);
        entity.setCheckIn(request.getCheckIn());
        entity.setCheckOut(request.getCheckOut());
        entity.setAdultCount(request.getAdultCount());
        entity.setChildCount(request.getChildCount());
        entity.setCurrencyEntity(currencyEntity);
        entity.setPriceUnitEntity(priceUnitEntity);
        entity.setTotalPrice(totalPrice);
        entity.setNotes(request.getNotes() != null ? request.getNotes() : "");
        return entity;
    }

    /** Builds the new row that supersedes {@code previous} — every field carried forward except status. */
    public ReservationEntity transition(ReservationEntity previous, ReservationStatusEntity newReservationStatusEntity) {
        ReservationEntity entity = new ReservationEntity();
        entity.setReservationStatusEntity(newReservationStatusEntity);
        entity.setReservationSourceEntity(previous.getReservationSourceEntity());
        entity.setCheckIn(previous.getCheckIn());
        entity.setCheckOut(previous.getCheckOut());
        entity.setAdultCount(previous.getAdultCount());
        entity.setChildCount(previous.getChildCount());
        entity.setCurrencyEntity(previous.getCurrencyEntity());
        entity.setPriceUnitEntity(previous.getPriceUnitEntity());
        entity.setTotalPrice(previous.getTotalPrice());
        entity.setNotes(previous.getNotes());
        entity.setPreviousReservationEntity(previous);
        return entity;
    }

    public ReservationDto.ReservationDtoBuilder toDto(ReservationEntity entity) {
        return ReservationDto.builder()
                .id(entity.getId())
                .customerId(entity.getUserEntity().getId())
                .bookingGroupId(entity.getBookingGroupEntity().getId())
                .checkIn(entity.getCheckIn())
                .checkOut(entity.getCheckOut())
                .previousReservationId(entity.getPreviousReservationEntity() != null
                        ? entity.getPreviousReservationEntity().getId() : null)
                .adultCount(entity.getAdultCount())
                .childCount(entity.getChildCount())
                .totalPrice(entity.getTotalPrice())
                .notes(entity.getNotes())
                .blocksAvailability(entity.getBlocksAvailability());
    }
}
