package com.example.resortbackendapplication1.resort.roomreservation.model.mapper;

import com.example.resortbackendapplication1.currency.model.entity.CurrencyEntity;
import com.example.resortbackendapplication1.price.model.entity.PriceUnitEntity;
import com.example.resortbackendapplication1.reservation.model.entity.ReservationStatusEntity;
import com.example.resortbackendapplication1.resort.pricing.PricingCalculator;
import com.example.resortbackendapplication1.resort.roomreservation.dto.request.roomreservation.CreateResortRoomReservationGuestRequest;
import com.example.resortbackendapplication1.resort.roomreservation.dto.request.roomreservation.CreateResortRoomReservationRequest;
import com.example.resortbackendapplication1.resort.roomreservation.model.dto.ResortRoomReservationDto;
import com.example.resortbackendapplication1.resort.roomreservation.model.dto.ResortRoomReservationGuestDto;
import com.example.resortbackendapplication1.resort.roomreservation.model.dto.ResortRoomReservationNightlyPriceDto;
import com.example.resortbackendapplication1.resort.roomreservation.model.entity.ResortRoomReservationEntity;
import com.example.resortbackendapplication1.resort.roomreservation.model.entity.ResortRoomReservationGuestEntity;
import com.example.resortbackendapplication1.resort.roomreservation.model.entity.ResortRoomReservationNightlyPriceEntity;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

/** Reservation is append-only: there is no update() here. */
@UtilityClass
public class ResortRoomReservationMapper {

    public ResortRoomReservationEntity create(CreateResortRoomReservationRequest request,
                                              ReservationStatusEntity reservationStatusEntity,
                                              CurrencyEntity currencyEntity,
                                              PriceUnitEntity priceUnitEntity,
                                              List<PricingCalculator.NightlyRate> nights) {
        ResortRoomReservationEntity entity = new ResortRoomReservationEntity();
        entity.setReservationStatusEntity(reservationStatusEntity);
        entity.setCheckIn(request.getCheckIn());
        entity.setCheckOut(request.getCheckOut());
        entity.setAdultCount(request.getAdultCount());
        entity.setChildCount(request.getChildCount());
        entity.setCurrencyEntity(currencyEntity);
        entity.setPriceUnitEntity(priceUnitEntity);
        entity.setTotalPrice(sumNightlyPrices(nights));
        entity.setNotes(request.getNotes() != null ? request.getNotes() : "");
        mapGuests(entity, request.getGuests());
        mapNightlyPrices(entity, nights);
        return entity;
    }

    private BigDecimal sumNightlyPrices(List<PricingCalculator.NightlyRate> nights) {
        return nights.stream()
                .map(PricingCalculator.NightlyRate::price)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private void mapGuests(ResortRoomReservationEntity entity, List<CreateResortRoomReservationGuestRequest> guests) {
        int sortOrder = 0;
        for (CreateResortRoomReservationGuestRequest guest : guests) {
            ResortRoomReservationGuestEntity guestEntity = new ResortRoomReservationGuestEntity();
            guestEntity.setName(guest.getName());
            guestEntity.setGuestType(guest.getGuestType());
            guestEntity.setSortOrder(sortOrder++);
            entity.addResortRoomReservationGuestEntity(guestEntity);
        }
    }

    private void mapNightlyPrices(ResortRoomReservationEntity entity, List<PricingCalculator.NightlyRate> nights) {
        for (PricingCalculator.NightlyRate night : nights) {
            ResortRoomReservationNightlyPriceEntity nightlyPriceEntity = new ResortRoomReservationNightlyPriceEntity();
            nightlyPriceEntity.setNightDate(night.date());
            nightlyPriceEntity.setRateType(night.rateType());
            nightlyPriceEntity.setPrice(night.price());
            entity.addResortRoomReservationNightlyPriceEntity(nightlyPriceEntity);
        }
    }

    private List<ResortRoomReservationGuestDto> activeGuestDtos(ResortRoomReservationEntity entity) {
        return entity.getResortRoomReservationGuestEntities().stream()
                .filter(guest -> Boolean.TRUE.equals(guest.getIsActive()) && Boolean.FALSE.equals(guest.getIsDeleted()))
                .sorted(Comparator.comparing(ResortRoomReservationGuestEntity::getSortOrder))
                .map(guest -> ResortRoomReservationGuestDto.builder()
                        .name(guest.getName())
                        .guestType(guest.getGuestType())
                        .build())
                .toList();
    }

    private List<ResortRoomReservationNightlyPriceDto> nightlyPriceDtos(ResortRoomReservationEntity entity) {
        return entity.getResortRoomReservationNightlyPriceEntities().stream()
                .filter(night -> Boolean.TRUE.equals(night.getIsActive()) && Boolean.FALSE.equals(night.getIsDeleted()))
                .sorted(Comparator.comparing(ResortRoomReservationNightlyPriceEntity::getNightDate))
                .map(night -> ResortRoomReservationNightlyPriceDto.builder()
                        .date(night.getNightDate())
                        .price(night.getPrice())
                        .rateType(night.getRateType())
                        .build())
                .toList();
    }

    public ResortRoomReservationDto.ResortRoomReservationDtoBuilder toDto(ResortRoomReservationEntity entity) {
        return ResortRoomReservationDto.builder()
                .id(entity.getId())
                .customerId(entity.getResortBookingEntity().getUserEntity().getId())
                .bookingId(entity.getResortBookingEntity().getId())
                .checkIn(entity.getCheckIn())
                .checkOut(entity.getCheckOut())
                .guests(activeGuestDtos(entity))
                .previousResortRoomReservationId(entity.getPreviousResortRoomReservationEntity() != null
                        ? entity.getPreviousResortRoomReservationEntity().getId() : null)
                .adultCount(entity.getAdultCount())
                .childCount(entity.getChildCount())
                .nights(nightlyPriceDtos(entity))
                .totalPrice(entity.getTotalPrice())
                .notes(entity.getNotes())
                .cancellationReason(entity.getCancellationReason())
                .blocksAvailability(entity.getBlocksAvailability());
    }
}
