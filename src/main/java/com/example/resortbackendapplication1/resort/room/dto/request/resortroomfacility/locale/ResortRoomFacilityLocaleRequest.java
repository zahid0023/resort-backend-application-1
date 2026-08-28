package com.example.resortbackendapplication1.resort.room.dto.request.resortroomfacility.locale;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResortRoomFacilityLocaleRequest {

    @NotBlank
    @Size(max = 255)
    private String name;

    private String description;

    private String notes;

    @NotNull
    private Integer sortOrder;
}
