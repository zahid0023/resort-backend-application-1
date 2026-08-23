package com.example.resortbackendapplication1.resort.dto.response.resortroomcategoryfacilities;

import com.example.resortbackendapplication1.resort.model.dto.ResortRoomCategoryFacilityDto;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResortRoomCategoryFacilityResponse {

    private final ResortRoomCategoryFacilityDto data;

    public ResortRoomCategoryFacilityResponse(ResortRoomCategoryFacilityDto data) {
        this.data = data;
    }
}
