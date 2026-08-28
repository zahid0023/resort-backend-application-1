package com.example.resortbackendapplication1.resort.room.dto.request.resortroomfacility.locale;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CreateResortRoomFacilityLocaleRequest extends ResortRoomFacilityLocaleRequest {

    @NotNull
    private Long localeId;
}
