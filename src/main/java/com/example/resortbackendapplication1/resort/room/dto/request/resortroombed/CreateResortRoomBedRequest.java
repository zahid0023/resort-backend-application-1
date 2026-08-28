package com.example.resortbackendapplication1.resort.room.dto.request.resortroombed;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CreateResortRoomBedRequest extends ResortRoomBedRequest {

    /** Platform bed type this row represents. Immutable after creation. */
    @NotNull
    private Long bedTypeId;
}
