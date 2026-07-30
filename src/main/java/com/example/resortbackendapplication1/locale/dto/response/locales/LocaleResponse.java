package com.example.resortbackendapplication1.locale.dto.response.locales;

import com.example.resortbackendapplication1.locale.model.dto.LocaleDto;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class LocaleResponse {
    private final LocaleDto data;

    public LocaleResponse(LocaleDto data) {
        this.data = data;
    }
}
