package com.example.resortbackendapplication1.resort.room.dto.request.resortroomfacilitygroup;

import com.example.resortbackendapplication1.resort.room.dto.request.resortroomfacilitygroup.locale.ResortRoomFacilityGroupLocaleRequest;
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
public class CreateResortRoomFacilityGroupRequest extends ResortRoomFacilityGroupRequest {

    /** Optional link to a platform facility group. Null creates a resort-room-defined custom group. Immutable after creation. */
    private Long facilityGroupId;

    /** Resort-room-scoped identifier, unique per resort room. Immutable after creation. */
    @NotBlank
    @Size(max = 100)
    private String code;

    @Valid
    @NotNull
    private ResortRoomFacilityGroupLocaleRequest locale;
}
