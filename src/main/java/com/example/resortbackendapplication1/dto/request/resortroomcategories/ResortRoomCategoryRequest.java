package com.example.resortbackendapplication1.dto.request.resortroomcategories;

import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResortRoomCategoryRequest {
    private String name;
    private String description;
    private Integer sortOrder;
}
