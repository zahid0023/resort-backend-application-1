package com.example.resortbackendapplication1.resort.dto.response.resortroomcategorybeds;

import com.example.resortbackendapplication1.resort.model.dto.ResortRoomCategoryBedDto;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResortRoomCategoryBedResponse {

    private final ResortRoomCategoryBedDto data;

    public ResortRoomCategoryBedResponse(ResortRoomCategoryBedDto data) {
        this.data = data;
    }
}
