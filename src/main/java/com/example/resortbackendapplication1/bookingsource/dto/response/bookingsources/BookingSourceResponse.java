package com.example.resortbackendapplication1.bookingsource.dto.response.bookingsources;

import com.example.resortbackendapplication1.bookingsource.model.dto.BookingSourceDto;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BookingSourceResponse {

    private final BookingSourceDto data;

    public BookingSourceResponse(BookingSourceDto data) {
        this.data = data;
    }
}
