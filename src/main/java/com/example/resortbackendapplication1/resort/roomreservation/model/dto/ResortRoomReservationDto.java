package com.example.resortbackendapplication1.resort.roomreservation.model.dto;

import com.example.resortbackendapplication1.currency.model.dto.CurrencyDto;
import com.example.resortbackendapplication1.price.model.dto.PriceUnitDto;
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
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResortRoomReservationDto {

    private Long id;

    private Long customerId;

    /** Every reservation belongs to a booking, even a lone single-room booking (a "group of one"). */
    private Long bookingId;

    private ResortRoomDto resortRoom;

    private ReservationStatusDto reservationStatus;

    private LocalDate checkIn;

    private LocalDate checkOut;

    /** Every occupant of this room — distinct from the booking's own customer. */
    private List<ResortRoomReservationGuestDto> guests;

    private Long previousResortRoomReservationId;

    private Integer adultCount;

    private Integer childCount;

    private CurrencyDto currency;

    private PriceUnitDto priceUnit;

    /** One entry per night of the stay, frozen at booking time — see ResortRoomReservationNightlyPriceDto. */
    private List<ResortRoomReservationNightlyPriceDto> nights;

    private BigDecimal totalPrice;

    private String notes;

    /** Set only on a row whose status transition was explained (e.g. CANCELLED/NO_SHOW); null otherwise. */
    private String cancellationReason;

    private Boolean blocksAvailability;
}
