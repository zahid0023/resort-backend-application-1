package com.example.resortbackendapplication1.uiblocksection.dto.response.uiblocksections;

import com.example.resortbackendapplication1.uiblocksection.model.dto.UiBlockSectionLocaleDto;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UiBlockSectionLocaleResponse {
    private final UiBlockSectionLocaleDto uiBlockSectionLocale;

    public UiBlockSectionLocaleResponse(UiBlockSectionLocaleDto uiBlockSectionLocale) {
        this.uiBlockSectionLocale = uiBlockSectionLocale;
    }
}
