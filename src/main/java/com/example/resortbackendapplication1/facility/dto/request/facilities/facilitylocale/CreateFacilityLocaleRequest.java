package com.example.resortbackendapplication1.facility.dto.request.facilities.facilitylocale;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CreateFacilityLocaleRequest extends FacilityLocaleRequest {

    @NotNull
    private Long localeId;
}
