package com.example.resortbackendapplication1.resort.dto.request.resortfacilitygroup.resortfacilitygrouplocale;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CreateResortFacilityGroupLocaleRequest extends ResortFacilityGroupLocaleRequest {

    @NotNull
    private Long localeId;
}
