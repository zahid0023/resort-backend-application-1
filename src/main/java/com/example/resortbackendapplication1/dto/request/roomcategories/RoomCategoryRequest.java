package com.example.resortbackendapplication1.dto.request.roomcategories;

import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RoomCategoryRequest {
    private String code;
    private String name;
    private String description;
    private Integer sortOrder;
}
