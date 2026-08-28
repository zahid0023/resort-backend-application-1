package com.example.resortbackendapplication1.resort.contact.dto.request.resortcontact;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CreateResortContactRequest extends ResortContactRequest {

    @NotNull
    private Long contactTypeId;

    @NotNull
    private Long communicationChannelId;
}
