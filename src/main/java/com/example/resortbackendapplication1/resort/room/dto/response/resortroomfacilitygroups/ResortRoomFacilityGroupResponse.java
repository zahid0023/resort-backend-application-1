package com.example.resortbackendapplication1.resort.room.dto.response.resortroomfacilitygroups;

import com.example.resortbackendapplication1.resort.room.model.dto.ResortRoomFacilityGroupDto;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResortRoomFacilityGroupResponse {

    private final ResortRoomFacilityGroupDto data;

    public ResortRoomFacilityGroupResponse(ResortRoomFacilityGroupDto data) {
        this.data = data;
    }
}
