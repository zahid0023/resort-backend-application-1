package com.example.resortbackendapplication1.resort.room.dto.request.resortroom;

import com.example.resortbackendapplication1.resort.room.dto.request.resortroom.locale.ResortRoomLocaleRequest;
import com.example.resortbackendapplication1.resort.room.dto.request.resortroombed.CreateResortRoomBedRequest;
import com.example.resortbackendapplication1.resort.room.dto.request.resortroommeta.CreateResortRoomMetaRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CreateResortRoomRequest extends ResortRoomRequest {

    /** Initial room status. Changed afterward only via PUT .../rooms/{id}/status, never via this resource's own update. */
    @NotNull
    private Long roomStatusId;

    /** Resort-scoped identifier, unique per resort. Immutable after creation. */
    @NotBlank
    @Size(max = 50)
    private String code;

    @Valid
    @NotNull
    private ResortRoomLocaleRequest locale;

    @Valid
    @NotNull
    private CreateResortRoomMetaRequest meta;

    /** At least one bed configuration is required. Additional beds can be added afterward via POST /beds. */
    @Valid
    @NotEmpty
    private List<CreateResortRoomBedRequest> beds;
}
