package com.example.resortbackendapplication1.resort.roomcategory.dto.response.resortroomcategoryfacilitygroups;

import com.example.resortbackendapplication1.resort.roomcategory.model.dto.ResortRoomCategoryFacilityGroupDto;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResortRoomCategoryFacilityGroupResponse {

    private final ResortRoomCategoryFacilityGroupDto data;

    public ResortRoomCategoryFacilityGroupResponse(ResortRoomCategoryFacilityGroupDto data) {
        this.data = data;
    }
}
