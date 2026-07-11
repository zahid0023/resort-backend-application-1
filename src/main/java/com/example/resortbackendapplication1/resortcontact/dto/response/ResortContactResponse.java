package com.example.resortbackendapplication1.resortcontact.dto.response;

import com.example.resortbackendapplication1.resortcontact.model.dto.ResortContactDto;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResortContactResponse {
    private final ResortContactDto data;

    public ResortContactResponse(ResortContactDto data) {
        this.data = data;
    }
}
