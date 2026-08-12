package com.example.resortbackendapplication1.resortroletype.dto.response.resortroletypes;

import com.example.resortbackendapplication1.resortroletype.model.dto.ResortRoleTypeDto;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResortRoleTypeResponse {
    private final ResortRoleTypeDto data;

    public ResortRoleTypeResponse(ResortRoleTypeDto data) {
        this.data = data;
    }
}
