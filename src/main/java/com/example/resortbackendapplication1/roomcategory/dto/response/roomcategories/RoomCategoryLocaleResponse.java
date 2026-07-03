package com.example.resortbackendapplication1.roomcategory.dto.response.roomcategories;

import com.example.resortbackendapplication1.roomcategory.model.dto.RoomCategoryLocaleDto;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RoomCategoryLocaleResponse {

    private final RoomCategoryLocaleDto data;

    public RoomCategoryLocaleResponse(RoomCategoryLocaleDto roomCategoryLocale) {
        this.data = roomCategoryLocale;
    }
}
