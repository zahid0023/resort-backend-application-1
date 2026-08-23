package com.example.resortbackendapplication1.resort.dto.request.resortroomcategorybed;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CreateResortRoomCategoryBedRequest extends ResortRoomCategoryBedRequest {

    /** Platform bed type this row represents. Immutable after creation. */
    @NotNull
    private Long bedTypeId;
}
