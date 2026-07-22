package com.example.resortbackendapplication1.bedtype.dto.response.bedtypes;

import com.example.resortbackendapplication1.bedtype.model.dto.BedTypeLocaleDto;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BedTypeLocaleResponse {
    private final BedTypeLocaleDto bedTypeLocale;

    public BedTypeLocaleResponse(BedTypeLocaleDto bedTypeLocale) {
        this.bedTypeLocale = bedTypeLocale;
    }
}
