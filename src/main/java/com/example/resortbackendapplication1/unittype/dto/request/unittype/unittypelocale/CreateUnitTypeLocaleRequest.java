package com.example.resortbackendapplication1.unittype.dto.request.unittype.unittypelocale;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CreateUnitTypeLocaleRequest extends UnitTypeLocaleRequest {

    @NotNull
    private Long localeId;
}
