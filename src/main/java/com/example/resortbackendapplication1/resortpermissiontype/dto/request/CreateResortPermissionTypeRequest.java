package com.example.resortbackendapplication1.resortpermissiontype.dto.request;

import com.example.resortbackendapplication1.resortpermissiontype.dto.request.locale.CreateResortPermissionTypeLocaleRequest;
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
public class CreateResortPermissionTypeRequest extends ResortPermissionTypeRequest {

    @NotBlank
    @Size(max = 150)
    private String code;

    private List<CreateResortPermissionTypeLocaleRequest> locales;
}
