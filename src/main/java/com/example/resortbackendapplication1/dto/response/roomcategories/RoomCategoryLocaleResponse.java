package com.example.resortbackendapplication1.dto.response.roomcategories;

import com.example.resortbackendapplication1.model.dto.RoomCategoryLocaleDto;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RoomCategoryLocaleResponse {
    private final RoomCategoryLocaleDto roomCategoryLocale;

    public RoomCategoryLocaleResponse(RoomCategoryLocaleDto roomCategoryLocale) {
        this.roomCategoryLocale = roomCategoryLocale;
    }
}
