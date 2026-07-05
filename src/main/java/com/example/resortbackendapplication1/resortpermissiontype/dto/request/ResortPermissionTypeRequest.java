package com.example.resortbackendapplication1.resortpermissiontype.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResortPermissionTypeRequest {

    @NotNull
    @Min(value = 1, message = "sort_order must be 1 or greater")
    private Integer sortOrder;
}
