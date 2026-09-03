package com.example.resortbackendapplication1.resort.mail.dto.response.resortmailconfigs;

import com.example.resortbackendapplication1.resort.mail.model.dto.ResortMailConfigDto;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResortMailConfigResponse {

    private final ResortMailConfigDto data;

    public ResortMailConfigResponse(ResortMailConfigDto data) {
        this.data = data;
    }
}
