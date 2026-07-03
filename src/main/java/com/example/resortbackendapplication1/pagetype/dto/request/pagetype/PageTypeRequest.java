package com.example.resortbackendapplication1.pagetype.dto.request.pagetype;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PageTypeRequest {

    @NotNull
    private Integer sortOrder;
}
