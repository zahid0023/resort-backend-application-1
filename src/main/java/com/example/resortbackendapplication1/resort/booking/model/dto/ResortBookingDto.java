package com.example.resortbackendapplication1.resort.booking.model.dto;

import com.example.resortbackendapplication1.bookingsource.model.dto.BookingSourceDto;
import com.example.resortbackendapplication1.currency.model.dto.CurrencyDto;
import com.example.resortbackendapplication1.resort.roomreservation.model.dto.ResortRoomReservationDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResortBookingDto {

    private Long id;

    /** e.g. "BK00000123" — quotable back by the customer over phone/WhatsApp instead of the raw id. */
    private String referenceCode;

    private BookingCustomerDto customer;

    private BookingSourceDto bookingSource;

    private String notes;

    /** One entry per room in the booking — room/category/dates/notes/nightly prices/guests/price live there. */
    private List<ResortRoomReservationDto> reservations;

    /** Sum of every reservation's own totalPrice — every reservation in a booking shares one currency. */
    private BigDecimal totalPrice;

    private CurrencyDto currency;
}
