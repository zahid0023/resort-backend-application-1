package com.example.resortbackendapplication1.uiblocksection.dto.request.uiblocksection;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UiBlockSectionRequest {

    @NotNull
    private Integer sortOrder;
}
