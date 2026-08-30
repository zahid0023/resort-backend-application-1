package com.example.resortbackendapplication1.resort.reservation.model.dto;

import com.example.resortbackendapplication1.currency.model.dto.CurrencyDto;
import com.example.resortbackendapplication1.price.model.dto.PriceUnitDto;
import com.example.resortbackendapplication1.reservation.model.dto.ReservationSourceDto;
import com.example.resortbackendapplication1.reservation.model.dto.ReservationStatusDto;
import com.example.resortbackendapplication1.resort.room.model.dto.ResortRoomDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ReservationDto {

    private Long id;

    private Long customerId;

    /** Every reservation belongs to a booking group, even a lone single-room booking (a "group of one"). */
    private Long bookingGroupId;

    private ResortRoomDto resortRoom;

    private ReservationStatusDto reservationStatus;

    private ReservationSourceDto reservationSource;

    private LocalDate checkIn;

    private LocalDate checkOut;

    private Long previousReservationId;

    private Integer adultCount;

    private Integer childCount;

    private CurrencyDto currency;

    private PriceUnitDto priceUnit;

    private BigDecimal totalPrice;

    private String notes;

    private Boolean blocksAvailability;
}
