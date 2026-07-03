package com.example.resortbackendapplication1.uiblocksection.dto.request.uiblocksection.uiblocksectionlocale;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CreateUiBlockSectionLocaleRequest extends UiBlockSectionLocaleRequest {

    @NotNull
    private Long localeId;
}
