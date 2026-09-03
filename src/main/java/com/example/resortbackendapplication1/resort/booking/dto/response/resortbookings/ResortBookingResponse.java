package com.example.resortbackendapplication1.resort.booking.dto.response.resortbookings;

import com.example.resortbackendapplication1.resort.booking.model.dto.ResortBookingDto;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResortBookingResponse {
    private final ResortBookingDto data;

    public ResortBookingResponse(ResortBookingDto data) {
        this.data = data;
    }
}
