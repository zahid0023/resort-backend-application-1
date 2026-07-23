package com.example.resortbackendapplication1.resort.dto.request.resortfacility;

import com.example.resortbackendapplication1.resort.dto.request.resortfacility.resortfacilitylocale.CreateResortFacilityLocaleRequest;
import com.example.resortbackendapplication1.resortfacilityprice.dto.request.CreateResortFacilityPriceRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CreateResortFacilityRequest extends ResortFacilityRequest {

    @NotNull
    private Long resortFacilityGroupId;

    @Valid
    private CreateResortFacilityPriceRequest resortFacilityPrice;

    private List<CreateResortFacilityLocaleRequest> locales;
}
