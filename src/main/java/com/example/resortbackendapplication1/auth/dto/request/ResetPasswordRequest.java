package com.example.resortbackendapplication1.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResetPasswordRequest {

    @NotBlank
    private String username;

    @NotBlank
    private String otp;

    @NotBlank
    private String newPassword;

    @NotBlank
    private String confirmNewPassword;
}
