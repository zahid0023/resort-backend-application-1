package com.example.resortbackendapplication1.resort.contact.dto.request.resortcontact;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResortContactRequest {

    @NotBlank
    private String contactValue;

    @NotNull
    private Boolean isPrimary;

    @NotNull
    private Integer sortOrder;
}
