package com.example.resortbackendapplication1.mail.provider.dto.request.mailprovider.config;

import com.example.resortbackendapplication1.commons.mail.MailProviderConfigCode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.util.Map;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MailProviderConfigRequest {

    @NotBlank
    @Size(max = 100)
    private String name;

    /** Optional — set only to designate this config as the one backing a given system flow. */
    private MailProviderConfigCode code;

    @NotNull
    private Map<String, Object> config;
}
