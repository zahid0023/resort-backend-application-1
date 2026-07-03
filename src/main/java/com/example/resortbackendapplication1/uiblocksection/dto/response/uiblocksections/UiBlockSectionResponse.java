package com.example.resortbackendapplication1.uiblocksection.dto.response.uiblocksections;

import com.example.resortbackendapplication1.uiblocksection.model.dto.UiBlockSectionDto;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UiBlockSectionResponse {
    private final UiBlockSectionDto uiBlockSection;

    public UiBlockSectionResponse(UiBlockSectionDto uiBlockSection) {
        this.uiBlockSection = uiBlockSection;
    }
}
