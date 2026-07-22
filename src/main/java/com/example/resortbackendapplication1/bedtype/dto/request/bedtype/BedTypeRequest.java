package com.example.resortbackendapplication1.bedtype.dto.request.bedtype;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BedTypeRequest {

    @NotNull
    private Integer sortOrder;
}
