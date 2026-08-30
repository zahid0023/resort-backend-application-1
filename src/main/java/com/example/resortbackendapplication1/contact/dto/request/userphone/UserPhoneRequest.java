package com.example.resortbackendapplication1.contact.dto.request.userphone;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserPhoneRequest {

    @NotBlank
    @Size(max = 50)
    private String phone;

    @NotNull
    private Boolean isWhatsapp;

    @NotNull
    private Boolean isPrimary;

    @NotNull
    private Integer sortOrder;
}
