package com.example.resortbackendapplication1.facility.dto.request.facilitygroup;

import com.example.resortbackendapplication1.facility.dto.request.facilitygroup.locale.FacilityGroupLocaleRequest;
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
public class CreateFacilityGroupRequest extends FacilityGroupRequest {

    @NotBlank
    @Size(max = 100)
    private String code;

    @Valid
    @NotNull
    private FacilityGroupLocaleRequest locale;

}
