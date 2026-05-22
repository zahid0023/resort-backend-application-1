package com.example.resortbackendapplication1.dto.request.facilities;

import com.example.resortbackendapplication1.dto.request.facilities.facilitylocale.CreateFacilityLocaleRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CreateFacilityRequest extends FacilityRequest {

    @NotBlank
    @Size(max = 100)
    private String code;

    private List<CreateFacilityLocaleRequest> locales;
}
