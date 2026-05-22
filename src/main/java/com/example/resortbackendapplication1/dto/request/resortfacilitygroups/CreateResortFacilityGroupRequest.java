package com.example.resortbackendapplication1.dto.request.resortfacilitygroups;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@EqualsAndHashCode(callSuper = true)
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CreateResortFacilityGroupRequest extends ResortFacilityGroupRequest {

    @NotNull(message = "facility_group_id must not be null")
    private Long facilityGroupId;

    @NotBlank(message = "name must not be blank")
    @Override
    public String getName() {
        return super.getName();
    }

    @NotNull(message = "description must not be null")
    @Override
    public String getDescription() {
        return super.getDescription();
    }

    @NotNull(message = "sort_order must not be null")
    @Override
    public Integer getSortOrder() {
        return super.getSortOrder();
    }
}
