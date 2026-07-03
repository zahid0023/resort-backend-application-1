package com.example.resortbackendapplication1.pagetype.dto.request.pagetype.pagetypelocale;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CreatePageTypeLocaleRequest extends PageTypeLocaleRequest {

    @NotNull
    private Long localeId;
}
