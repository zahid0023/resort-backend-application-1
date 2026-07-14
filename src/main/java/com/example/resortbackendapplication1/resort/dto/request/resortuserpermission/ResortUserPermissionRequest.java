package com.example.resortbackendapplication1.resort.dto.request.resortuserpermission;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Builder
public class ResortUserPermissionRequest {

    @NotNull
    private Long resortPermissionTypeId;

    @NotNull
    private Boolean isAllowed;
}
