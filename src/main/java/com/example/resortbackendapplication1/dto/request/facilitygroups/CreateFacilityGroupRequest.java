package com.example.resortbackendapplication1.dto.request.facilitygroups;

import com.example.resortbackendapplication1.enums.IconType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@EqualsAndHashCode(callSuper = true)
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CreateFacilityGroupRequest extends FacilityGroupRequest {

    @NotBlank(message = "code must not be blank")
    @Override
    public String getCode() {
        return super.getCode();
    }

    @NotBlank(message = "name must not be blank")
    @Override
    public String getName() {
        return super.getName();
    }

    @NotNull(message = "icon_type must not be null")
    @Override
    public IconType getIconType() {
        return super.getIconType();
    }
}
