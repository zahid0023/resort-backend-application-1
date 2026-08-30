package com.example.resortbackendapplication1.resort.booking.dto.response.bookinggroups;

import com.example.resortbackendapplication1.resort.booking.model.dto.BookingGroupDto;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BookingGroupResponse {

    private final BookingGroupDto data;

    public BookingGroupResponse(BookingGroupDto data) {
        this.data = data;
    }
}
