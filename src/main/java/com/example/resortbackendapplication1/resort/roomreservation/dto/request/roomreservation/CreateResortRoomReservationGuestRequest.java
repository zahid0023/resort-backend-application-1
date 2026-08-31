package com.example.resortbackendapplication1.resort.roomreservation.dto.request.roomreservation;

import com.example.resortbackendapplication1.resort.roomreservation.model.enums.GuestType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

/** One occupant of a room within a CreateResortRoomReservationRequest's guest list. */
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CreateResortRoomReservationGuestRequest {

    @NotBlank
    private String name;

    @NotNull
    private GuestType guestType;
}
