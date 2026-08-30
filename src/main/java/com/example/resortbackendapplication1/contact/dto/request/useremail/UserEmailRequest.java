package com.example.resortbackendapplication1.contact.dto.request.useremail;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserEmailRequest {

    @NotBlank
    @Size(max = 255)
    private String email;

    @NotNull
    private Boolean isPrimary;

    @NotNull
    private Integer sortOrder;
}
