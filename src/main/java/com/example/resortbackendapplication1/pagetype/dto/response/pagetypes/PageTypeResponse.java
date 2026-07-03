package com.example.resortbackendapplication1.pagetype.dto.response.pagetypes;

import com.example.resortbackendapplication1.pagetype.model.dto.PageTypeDto;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PageTypeResponse {
    private final PageTypeDto pageType;

    public PageTypeResponse(PageTypeDto pageType) {
        this.pageType = pageType;
    }
}
