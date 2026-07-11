package com.example.resortbackendapplication1.resortcontact.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class CreateResortContactRequest extends ResortContactRequest {

    @NotNull
    private Long contactTypeId;

    @NotNull
    private Long communicationChannelId;
}
