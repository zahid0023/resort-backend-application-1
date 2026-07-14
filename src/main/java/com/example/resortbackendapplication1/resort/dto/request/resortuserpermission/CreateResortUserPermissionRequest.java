package com.example.resortbackendapplication1.resort.dto.request.resortuserpermission;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CreateResortUserPermissionRequest {

    @NotNull
    @NotEmpty
    private List<ResortUserPermissionRequest> permissions;
}
