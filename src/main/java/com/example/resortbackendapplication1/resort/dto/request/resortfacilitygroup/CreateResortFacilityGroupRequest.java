package com.example.resortbackendapplication1.resort.dto.request.resortfacilitygroup;

import com.example.resortbackendapplication1.resort.dto.request.resortfacilitygroup.resortfacilitygrouplocale.CreateResortFacilityGroupLocaleRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CreateResortFacilityGroupRequest extends ResortFacilityGroupRequest {

    private List<CreateResortFacilityGroupLocaleRequest> locales;
}
