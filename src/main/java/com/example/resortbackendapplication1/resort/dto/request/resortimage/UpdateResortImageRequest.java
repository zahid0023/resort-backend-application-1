package com.example.resortbackendapplication1.resort.dto.request.resortimage;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UpdateResortImageRequest {
    private String caption;

    @NotNull
    private Integer sortOrder;
}
