package com.example.resortbackendapplication1.resortroletype.dto.request.resortroletype;

import com.example.resortbackendapplication1.resortroletype.dto.request.resortroletype.locale.ResortRoleTypeLocaleRequest;
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
public class CreateResortRoleTypeRequest extends ResortRoleTypeRequest {

    @NotBlank
    @Size(max = 100)
    private String code;

    @Valid
    @NotNull
    private ResortRoleTypeLocaleRequest locale;

}
