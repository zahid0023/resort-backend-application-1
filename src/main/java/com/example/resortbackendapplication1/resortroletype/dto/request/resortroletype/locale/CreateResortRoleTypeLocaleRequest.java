package com.example.resortbackendapplication1.resortroletype.dto.request.resortroletype.locale;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CreateResortRoleTypeLocaleRequest extends ResortRoleTypeLocaleRequest {

    @NotNull
    private Long localeId;

}
