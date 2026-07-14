package com.example.resortbackendapplication1.resort.dto.response;

import com.example.resortbackendapplication1.resort.model.dto.ResortUserDto;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResortUserResponse {
    private final ResortUserDto resortUser;

    public ResortUserResponse(ResortUserDto resortUser) {
        this.resortUser = resortUser;
    }
}
