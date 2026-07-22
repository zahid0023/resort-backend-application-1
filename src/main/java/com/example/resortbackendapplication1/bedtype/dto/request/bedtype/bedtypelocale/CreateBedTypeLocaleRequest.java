package com.example.resortbackendapplication1.bedtype.dto.request.bedtype.bedtypelocale;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CreateBedTypeLocaleRequest extends BedTypeLocaleRequest {

    @NotNull
    private Long localeId;
}
