package com.example.resortbackendapplication1.resort.dto.request.resortfacility;

import com.example.resortbackendapplication1.resort.dto.request.resortfacility.locale.ResortFacilityLocaleRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CreateResortFacilityRequest extends ResortFacilityRequest {

    /** Optional link to a platform facility. Null creates a resort-defined custom facility. Immutable after creation. */
    private Long facilityId;

    /** Resort-scoped identifier, unique per resort. Immutable after creation. */
    @NotBlank
    @Size(max = 100)
    private String code;

    @Valid
    @NotNull
    private ResortFacilityLocaleRequest locale;
}
