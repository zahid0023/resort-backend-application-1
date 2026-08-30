package com.example.resortbackendapplication1.resort.booking.model.dto;

import com.example.resortbackendapplication1.resort.reservation.model.dto.ReservationDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BookingGroupDto {

    private Long id;

    private Long customerId;

    /** Every active reservation tagged with this booking group — one per room booked in the same transaction. */
    private List<ReservationDto> reservations;
}
