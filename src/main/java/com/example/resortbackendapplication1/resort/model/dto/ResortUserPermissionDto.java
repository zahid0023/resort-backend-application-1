package com.example.resortbackendapplication1.resort.model.dto;

import lombok.Builder;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResortUserPermissionDto {
    private Long id;
    private Long resortUserId;
    private Long resortPermissionTypeId;
    private Boolean isAllowed;
}
