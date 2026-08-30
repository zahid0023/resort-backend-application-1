package com.example.resortbackendapplication1.resort.booking.dto.request.booking;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

/** One room within a multi-room CreateBookingRequest — check_in/check_out/status/source/customer are shared. */
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CreateBookingRoomEntryRequest {

    @NotNull
    private Long resortRoomCategoryId;

    @NotNull
    private Long roomId;

    @NotNull
    @Min(1)
    private Integer adultCount;

    @NotNull
    @Min(0)
    private Integer childCount;

    @NotNull
    private Long currencyId;
}
