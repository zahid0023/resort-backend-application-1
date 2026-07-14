package com.example.resortbackendapplication1.resort.model.dto;

import lombok.Builder;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.time.Instant;

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResortUserDto {
    private Long id;
    private Long resortId;
    private Long userId;
    private Long resortAccessTypeId;
    private Instant joinedAt;
}
