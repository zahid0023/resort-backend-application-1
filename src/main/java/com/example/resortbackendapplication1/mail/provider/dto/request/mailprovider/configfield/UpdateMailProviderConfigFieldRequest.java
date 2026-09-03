package com.example.resortbackendapplication1.mail.provider.dto.request.mailprovider.configfield;

import lombok.Data;
import lombok.EqualsAndHashCode;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UpdateMailProviderConfigFieldRequest extends MailProviderConfigFieldRequest {
}
