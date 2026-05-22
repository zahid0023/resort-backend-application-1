package com.example.resortbackendapplication1.dto.request.roomcategories.roomcategorylocale;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CreateRoomCategoryLocaleRequest extends RoomCategoryLocaleRequest {

    @NotNull
    private Long localeId;
}
