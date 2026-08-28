package com.example.resortbackendapplication1.resort.room.dto.request.resortroomfacility;

import com.example.resortbackendapplication1.resort.room.dto.request.resortroomfacility.locale.ResortRoomFacilityLocaleRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CreateResortRoomFacilityRequest extends ResortRoomFacilityRequest {

    /** Resort room facility group this facility belongs to. Required. Immutable after creation. */
    @NotNull
    private Long resortRoomFacilityGroupId;

    /** Optional link to a platform facility. Null creates a resort-room-defined custom facility. Immutable after creation. */
    private Long facilityId;

    /** Resort-room-scoped identifier, unique per resort room. Immutable after creation. */
    @NotBlank
    @Size(max = 100)
    private String code;

    @Valid
    @NotNull
    private ResortRoomFacilityLocaleRequest locale;
}
