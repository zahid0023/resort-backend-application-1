package com.example.resortbackendapplication1.resortroomcategory.dto.response;

import com.example.resortbackendapplication1.resortroomcategory.model.dto.ResortRoomCategoryMetaDto;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResortRoomCategoryMetaResponse {
    private final ResortRoomCategoryMetaDto data;

    public ResortRoomCategoryMetaResponse(ResortRoomCategoryMetaDto meta) {
        this.data = meta;
    }
}
