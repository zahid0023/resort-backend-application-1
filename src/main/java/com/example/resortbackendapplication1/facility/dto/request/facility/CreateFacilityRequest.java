package com.example.resortbackendapplication1.facility.dto.request.facility;

import com.example.resortbackendapplication1.facility.dto.request.facility.locale.FacilityLocaleRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CreateFacilityRequest extends FacilityRequest {

    @NotBlank
    @Size(max = 100)
    private String code;

    @NotEmpty
    private Set<Long> facilityGroupIds;

    @NotEmpty
    private Set<Long> facilityScopeIds;

    @Valid
    @NotNull
    private FacilityLocaleRequest locale;

}
