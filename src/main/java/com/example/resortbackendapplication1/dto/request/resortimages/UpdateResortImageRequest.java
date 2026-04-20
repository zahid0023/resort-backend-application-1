package com.example.resortbackendapplication1.dto.request.resortimages;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UpdateResortImageRequest {
    private String caption;
    private Boolean isDefault;
    private Integer sortOrder;
}
