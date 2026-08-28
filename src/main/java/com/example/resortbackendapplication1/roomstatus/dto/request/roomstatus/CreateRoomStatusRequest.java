package com.example.resortbackendapplication1.roomstatus.dto.request.roomstatus;

import com.example.resortbackendapplication1.roomstatus.dto.request.roomstatus.locale.RoomStatusLocaleRequest;
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
public class CreateRoomStatusRequest extends RoomStatusRequest {

    @NotBlank
    @Size(max = 50)
    private String code;

    @Valid
    @NotNull
    private RoomStatusLocaleRequest locale;

}
