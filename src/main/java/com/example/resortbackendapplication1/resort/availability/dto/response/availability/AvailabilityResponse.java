package com.example.resortbackendapplication1.resort.availability.dto.response.availability;

import com.example.resortbackendapplication1.resort.availability.model.dto.AvailableRoomDto;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AvailabilityResponse {

    private final List<AvailableRoomDto> data;

    public AvailabilityResponse(List<AvailableRoomDto> data) {
        this.data = data;
    }
}
