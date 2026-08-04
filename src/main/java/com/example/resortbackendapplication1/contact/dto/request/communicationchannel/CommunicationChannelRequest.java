package com.example.resortbackendapplication1.contact.dto.request.communicationchannel;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CommunicationChannelRequest {

    @NotNull
    private Integer sortOrder;

    @NotNull
    private Boolean isUrl;

    @NotNull
    private Boolean isPhone;

    @NotNull
    private Boolean isEmail;

    @NotNull
    private Boolean isClickable;

}
