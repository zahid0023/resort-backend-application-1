package com.example.resortbackendapplication1.resort.availability.model.dto;

import com.example.resortbackendapplication1.resort.room.model.dto.ResortRoomDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AvailableRoomDto {

    private ResortRoomDto room;

    private AvailableRoomPriceDto price;
}
