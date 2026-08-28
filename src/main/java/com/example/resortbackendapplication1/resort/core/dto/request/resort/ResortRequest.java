package com.example.resortbackendapplication1.resort.core.dto.request.resort;

import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResortRequest {
    // Resort has no fields shared between create and update — code is Create-only.
}
