package com.example.resortbackendapplication1.resortpermissiontype.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UpdateResortPermissionTypeRequest extends ResortPermissionTypeRequest {
}
