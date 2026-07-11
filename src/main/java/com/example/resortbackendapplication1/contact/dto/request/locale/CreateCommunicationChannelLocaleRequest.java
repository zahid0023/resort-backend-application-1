package com.example.resortbackendapplication1.contact.dto.request.locale;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class CreateCommunicationChannelLocaleRequest extends CommunicationChannelLocaleRequest {

    @NotNull
    private Long localeId;
}
