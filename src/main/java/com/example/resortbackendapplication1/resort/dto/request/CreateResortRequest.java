package com.example.resortbackendapplication1.resort.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CreateResortRequest extends ResortRequest {

    @NotBlank
    @Size(max = 100)
    private String code;
}
