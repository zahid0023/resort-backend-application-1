package com.example.resortbackendapplication1.pagetype.dto.response.pagetypes;

import com.example.resortbackendapplication1.pagetype.model.dto.PageTypeLocaleDto;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PageTypeLocaleResponse {
    private final PageTypeLocaleDto pageTypeLocale;

    public PageTypeLocaleResponse(PageTypeLocaleDto pageTypeLocale) {
        this.pageTypeLocale = pageTypeLocale;
    }
}
