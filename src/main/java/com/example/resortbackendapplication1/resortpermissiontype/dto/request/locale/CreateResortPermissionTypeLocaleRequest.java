package com.example.resortbackendapplication1.resortpermissiontype.dto.request.locale;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CreateResortPermissionTypeLocaleRequest extends ResortPermissionTypeLocaleRequest {

    @NotNull
    private Long localeId;
}
