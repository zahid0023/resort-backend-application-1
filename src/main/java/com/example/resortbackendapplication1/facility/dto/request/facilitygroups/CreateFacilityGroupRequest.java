package com.example.resortbackendapplication1.facility.dto.request.facilitygroups;

import com.example.resortbackendapplication1.facility.dto.request.facilitygroups.facilitygrouplocale.CreateFacilityGroupLocaleRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CreateFacilityGroupRequest extends FacilityGroupRequest {

    @NotBlank
    @Size(max = 100)
    private String code;

    private List<CreateFacilityGroupLocaleRequest> locales;

    @NotEmpty
    private List<Long> scopeIds;
}
