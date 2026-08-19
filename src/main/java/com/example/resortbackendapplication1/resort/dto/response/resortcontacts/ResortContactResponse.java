package com.example.resortbackendapplication1.resort.dto.response.resortcontacts;

import com.example.resortbackendapplication1.resort.model.dto.ResortContactDto;
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
