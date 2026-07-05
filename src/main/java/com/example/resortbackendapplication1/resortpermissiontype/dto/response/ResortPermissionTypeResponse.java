package com.example.resortbackendapplication1.resortpermissiontype.dto.response;

import com.example.resortbackendapplication1.resortpermissiontype.model.dto.ResortPermissionTypeDto;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResortPermissionTypeResponse {
    private final ResortPermissionTypeDto data;

    public ResortPermissionTypeResponse(ResortPermissionTypeDto resortPermissionType) {
        this.data = resortPermissionType;
    }
}
