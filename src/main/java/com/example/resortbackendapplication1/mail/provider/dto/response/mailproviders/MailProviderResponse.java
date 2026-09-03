package com.example.resortbackendapplication1.mail.provider.dto.response.mailproviders;

import com.example.resortbackendapplication1.mail.provider.model.dto.MailProviderDto;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MailProviderResponse {
    private final MailProviderDto data;

    public MailProviderResponse(MailProviderDto data) {
        this.data = data;
    }
}
