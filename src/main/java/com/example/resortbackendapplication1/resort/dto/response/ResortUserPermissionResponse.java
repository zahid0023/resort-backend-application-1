package com.example.resortbackendapplication1.resort.dto.response;

import com.example.resortbackendapplication1.resort.model.dto.ResortUserPermissionDto;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResortUserPermissionResponse {
    private final ResortUserPermissionDto resortUserPermission;

    public ResortUserPermissionResponse(ResortUserPermissionDto resortUserPermission) {
        this.resortUserPermission = resortUserPermission;
    }
}
