package com.example.resortbackendapplication1.resort.dto.request.resortuser;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CreateResortUserRequest {

    @NotNull
    private Long userId;

    @NotNull
    private Long resortAccessTypeId;
}
