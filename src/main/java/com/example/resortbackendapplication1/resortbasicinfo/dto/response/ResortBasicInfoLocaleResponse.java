package com.example.resortbackendapplication1.resortbasicinfo.dto.response;

import com.example.resortbackendapplication1.resortbasicinfo.model.dto.ResortBasicInfoLocaleDto;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResortBasicInfoLocaleResponse {
    private final ResortBasicInfoLocaleDto resortBasicInfoLocale;

    public ResortBasicInfoLocaleResponse(ResortBasicInfoLocaleDto resortBasicInfoLocale) {
        this.resortBasicInfoLocale = resortBasicInfoLocale;
    }
}
